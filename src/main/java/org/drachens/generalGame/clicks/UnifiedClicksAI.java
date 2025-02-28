package org.drachens.generalGame.clicks;

import lombok.Data;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.timer.Task;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Countries.countryClass.Research;
import org.drachens.dataClasses.Diplomacy.Justifications.WarGoalType;
import org.drachens.dataClasses.Diplomacy.Justifications.WarGoalTypeEnum;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Vault;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.dataClasses.Research.ResearchCountry;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.events.countries.warjustification.WarJustificationStartEvent;
import org.drachens.generalGame.factory.Factory;
import org.drachens.generalGame.troops.TroopPathing;
import org.drachens.interfaces.ai.AI;
import org.drachens.interfaces.ai.QLearning;
import org.drachens.util.AStarPathfinderXZ;

import java.io.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class UnifiedClicksAI extends AI {
    private final Country country;
    private final Random r = new Random();
    private final Map<Province, List<Province>> availableSpaces = new HashMap<>();
    private final List<Province> centers = new ArrayList<>();
    private final ClickWarSystem clickWarSystem;
    private final TechTree tree;
    private final AStarPathfinderXZ aStarPathfinderXZ;
    private final ProvinceManager provinceManager;
    private final QLearning qLearning = new QLearning();
    private final Payment payment = new Payment(CurrencyEnum.production, 1);
    private final ResearchCenter researchCenter;
    private final Factory factory;
    private static final String Q_TABLE_SAVE_PATH = "qtable.ser";
    private boolean capped = false;
    private int tickCounter = 0;

    public UnifiedClicksAI(Country country, ClickWarSystem clickWarSystem) {
        this.country = country;
        this.clickWarSystem = clickWarSystem;
        this.aStarPathfinderXZ = country.getMilitary().getAStarPathfinder();
        this.tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
        this.provinceManager = ContinentalManagers.world(country.getInstance()).provinceManager();
        this.researchCenter = (ResearchCenter) BuildingEnum.researchCenter.getBuildTypes();
        this.factory = (Factory) BuildingEnum.factory.getBuildTypes();

        country.setAi(this);
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveQTable));
        loadQTable();
    }

    @Override
    public void tick() {
        if (capped) return;

        GameState state = gatherGameState();
        String stateKey = state.toString();
        UnifiedClicksAI.Action.ActionType actionType = qLearning.chooseAction(stateKey);

        ResearchOption researchOption = actionType == Action.ActionType.RESEARCH ? selectResearchOption() : null;
        Country targetCountry = actionType == Action.ActionType.CONQUER ? selectTargetCountry() : null;

        Action action = new Action(actionType, researchOption, targetCountry);
        double reward = executeAndGetReward(action);

        if (countActiveWars() > 1) reward -= 10 * countActiveWars();
        if (country.getInfo().isCapitulated()) {
            qLearning.updateQValue(stateKey, actionType, -1000, state.toString());
            capped = true;
        } else {
            qLearning.updateQValue(stateKey, actionType, reward, gatherGameState().toString());
        }

        if (++tickCounter % 100 == 0) saveQTable();
    }

    @Override
    public void fasterTick() {
        if (capped) return;

        if (shouldDefend()) updateQTableWithAction(new Action(Action.ActionType.DEFEND, null, null));

        Country targetCountry = selectTargetCountry();
        if (targetCountry != null && shouldAttack(targetCountry)) {
            Action attackAction = new Action(r.nextBoolean() ? Action.ActionType.START_WAR : Action.ActionType.CONQUER, null, targetCountry);
            updateQTableWithAction(attackAction);
        }
    }

    private void updateQTableWithAction(Action action) {
        double reward = executeAndGetReward(action);
        qLearning.updateQValue(gatherGameState().toString(), action.type(), reward, gatherGameState().toString());
    }

    private ResearchOption selectResearchOption() {
        if (country.getResearch() == null) return null;

        ResearchCountry researchCountry = country.getResearch().researchCountry();
        return tree.getResearchOption(tree.getAvailable(researchCountry)
                .stream().skip(r.nextInt(tree.getAvailable(researchCountry).size()))
                .findFirst().orElse(null));
    }

    private boolean shouldDefend() {
        return !attackedAt.isEmpty();
    }

    private boolean shouldAttack(Country targetCountry) {
        return targetCountry != null && !targetCountry.getInfo().isCapitulated() && !country.isAtWar(targetCountry.getName());
    }

    @SuppressWarnings("unchecked")
    private void loadQTable() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(Q_TABLE_SAVE_PATH))) {
            qLearning.setQTable((HashMap<String, HashMap<Action.ActionType, Double>>) in.readObject());
        } catch (IOException | ClassNotFoundException ignored) {}
    }

    private void saveQTable() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Q_TABLE_SAVE_PATH))) {
            out.writeObject(qLearning.getQTable());
        } catch (IOException ignored) {}
    }

    private GameState gatherGameState() {
        return new GameState(country);
    }

    private double executeAndGetReward(Action action) {
        return switch (action.type()) {
            case CONQUER -> attemptConquest(action.targetCountry()) ? 15 : -1;
            case START_WAR -> startWar();
            case RESEARCH -> startResearch(action.researchOption()) ? 2 : -2;
            case BUILD_FACTORY -> { buildFactory(country.getMilitary().getCities()); yield 1; }
            case BUILD_RESEARCH -> { buildResearch(); yield 1; }
            case DEFEND -> defendTerritory() ? 10 : -3;
            case NOTHING -> 0;
        };
    }

    private double startWar() {
        List<String> borders = new ArrayList<>(country.getMilitary().getBorders());
        if (borders.isEmpty()) return -5;

        List<Country> potentialTargets = borders.stream()
                .map(name -> ContinentalManagers.world(country.getInstance()).countryDataManager().getCountryFromName(name))
                .filter(target -> target != null
                        && country.canJustifyAgainst(target)
                        && country.getEconomy().getVault().getAmount(CurrencyEnum.production.getCurrencyType()) > target.getEconomy().getVault().getAmount(CurrencyEnum.production.getCurrencyType()))
                .sorted(Comparator.comparingInt(target -> target.getMilitary().getOccupies().size()))
                .toList();

        if (potentialTargets.isEmpty()) return -5;

        Country target = potentialTargets.getFirst();

        WarGoalType w = WarGoalTypeEnum.surprise.getWarGoalType();
        WarJustification warJustification = new WarJustification(country, target, w.modifier(), w.timeToMake(), w.expires(), true, country.getInstance());
        EventDispatcher.call(new WarJustificationStartEvent(warJustification, country));
        return 5;
    }

    private boolean defendTerritory() {
        if (attackedAt.isEmpty()) {
            return false;
        }

        List<Province> toRemove = new ArrayList<>();

        for (Province attackedProvince : new ArrayList<>(attackedAt)) {
            if (attackedProvince.getOccupier() == country) {
                toRemove.add(attackedProvince);
                continue;
            }

            if (isValidAdjacentProvince(attackedProvince)) {
                if (captureProvince(attackedProvince)) {
                    toRemove.add(attackedProvince);
                    return true;
                }
            }
        }

        attackedAt.removeAll(toRemove);
        return !toRemove.isEmpty();
    }


    public void buildResearch() {
        if (country.getResearch()==null)return;
        if (availableSpaces.isEmpty()) {
            List<Province> provinces = country.getMilitary().getOccupies();
            if (provinces.isEmpty())return;
            Province province = provinces.get(r.nextInt(provinces.size()));
            if (researchCenter.canBuild(country, province, null)) {
                researchCenter.forceBuild(country, province, null);
                availableSpaces.put(province, new ArrayList<>(province.getNeighbours()));
                centers.add(province);
            }
        } else {
            Province p = centers.get(new Random().nextInt(centers.size()));
            List<Province> prov = availableSpaces.get(p);
            if (prov.isEmpty()) {
                availableSpaces.remove(p);
                centers.remove(p);
                return;
            }
            Province selected = prov.get(new Random().nextInt(prov.size()));
            switch (new Random().nextInt(3)) {
                case 0:
                    BuildTypes b = BuildingEnum.researchLab.getBuildTypes();
                    if (b.canBuild(country, selected, null)) {
                        b.forceBuild(country, selected, null);
                    }
                    break;
                case 1:
                    BuildTypes bc = BuildingEnum.library.getBuildTypes();
                    if (bc.canBuild(country, selected, null)) {
                        bc.forceBuild(country, selected, null);
                    }
                    break;
                case 2:
                    BuildTypes bd = BuildingEnum.university.getBuildTypes();
                    if (bd.canBuild(country, selected, null)) {
                        bd.forceBuild(country, selected, null);
                    }
                    break;
            }
            prov.remove(selected);
            availableSpaces.put(p, prov);
        }
    }

    private void buildFactory(List<Province> cities) {
        if (cities.isEmpty()) {
            return;
        }
        final Province province = cities.get(this.r.nextInt(cities.size()));
        if (null != province.getBuilding()) {
            if (this.factory.requirementsToUpgrade(province.getBuilding(), this.country, 1, null))
                this.factory.upgrade(1, province.getBuilding(), this.country, null);
        } else if (this.factory.canBuild(this.country, province, null))
            this.factory.forceBuild(this.country, province, null);
        else {
            cities.remove(province);
            this.buildFactory(cities);
        }
        return;
    }

    private boolean startResearch(ResearchOption researchOption) {
        if (country.getResearch()==null) return false;
        ResearchCountry researchCountry = country.getResearch().researchCountry();
        if (researchOption.canResearch(researchCountry)) {
            researchCountry.startResearching(researchOption);
            return true;
        }
        return false;
    }
    Task task;

    private boolean attemptConquest(Country target) {
        if (target==null)return false;
        List<Province> targetCities = target.getMilitary().getCities();
        targetCities.sort(Comparator.comparingDouble(city -> country.getInfo().getCapital().distance(city)));
        List<Province> bord = new ArrayList<>();
        country.getMilitary().getBordersWars().forEach(bordrr -> bord.addAll(country.getMilitary().getBorder(bordrr)));

        for (Province city : targetCities) {
            for (Province borderProvince : bord) {
                List<AStarPathfinderXZ.Node> path = aStarPathfinderXZ.findPath(borderProvince, city, country, new TroopPathing());
                if (path != null && !path.isEmpty()) {
                    task = MinecraftServer.getSchedulerManager().buildTask(()->{
                        for (AStarPathfinderXZ.Node node : path) {
                            Province province = node.province;
                            if (province.getOccupier() == country) {
                                continue;
                            }

                            if (isValidAdjacentProvince(province)) {
                                if (!captureProvince(province)) {
                                    task.cancel();
                                    return;
                                }
                            } else {
                                if (!attemptCaptureInSurroundingArea(province)) {
                                    task.cancel();
                                    return;
                                }
                            }
                        }
                        task.cancel();
                    }).repeat(200L, ChronoUnit.MILLIS).schedule();
                    return true;
                }
            }
        }
        return false;
    }

    private boolean attemptCaptureInSurroundingArea(Province centerProvince) {
        Pos centerPos = centerProvince.getPos();
        int centerX = centerPos.blockX();
        int centerZ = centerPos.blockZ();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 && dz == 0) continue;

                Province adjacentProvince = provinceManager.getProvince(centerX + dx, centerZ + dz);
                if (adjacentProvince != null && adjacentProvince.getOccupier() != country) {
                    if (isValidAdjacentProvince(adjacentProvince)) {
                        if (captureProvince(adjacentProvince)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    private boolean isValidAdjacentProvince(Province province) {
        return clickWarSystem.AdjacentBlocks(province.getPos(), country, country.getInstance()) != null;
    }

    private int countActiveWars() {
        return country.getMilitary().getBordersWars().size();
    }

    public boolean captureProvince(Province province) {
        if (clickWarSystem.canCapture(province, country) != null && country.canMinusCost(payment)) {
            province.setOccupier(country);
            country.removePayment(payment);

            return true;
        }
        return false;
    }

    private Country selectTargetCountry() {
        List<String> borderingCountries = country.getMilitary().getBorders().stream().toList();
        if (borderingCountries.isEmpty()) {
            return null;
        }
        String targetName = borderingCountries.get(r.nextInt(borderingCountries.size()));
        return ContinentalManagers.world(country.getInstance()).countryDataManager().getCountryFromName(targetName);
    }

    @Data
    private static class GameState {
        private final boolean researchEnabled;
        private final boolean capitulated;
        private final HashSet<String> wars;
        private final List<Province> warBorders = new ArrayList<>();
        private final Vault vault;
        private final Research research;
        private final float capPerc;

        public GameState(Country country) {
            wars = country.getMilitary().getBordersWars();
            wars.forEach(war -> warBorders.addAll(country.getMilitary().getBorder(war)));
            capitulated = country.getInfo().isCapitulated();
            researchEnabled = country.getResearch() != null;
            if (researchEnabled) {
                research = country.getResearch();
            } else {
                research = null;
            }
            vault = country.getEconomy().getVault();
            capPerc = country.getInfo().getCapitulationPoints() / country.getInfo().getMaxCapitulationPoints();
        }
    }

    public record Action(UnifiedClicksAI.Action.ActionType type, ResearchOption researchOption,
                         Country targetCountry) {
        public enum ActionType {
            BUILD_FACTORY,
            BUILD_RESEARCH,
            RESEARCH,
            START_WAR,
            CONQUER,
            DEFEND,
            NOTHING
        }
    }
}