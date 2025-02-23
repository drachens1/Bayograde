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

public class UnifiedAI extends AI {
    private final Country country;
    private final Random r = new Random();
    private final HashMap<Province, List<Province>> availableSpaces = new HashMap<>();
    private final ResearchCenter researchCenter = (ResearchCenter) BuildingEnum.researchCenter.getBuildTypes();
    private final List<Province> centers = new ArrayList<>();
    private final ClickWarSystem clickWarSystem;
    private final Payment payment = new Payment(CurrencyEnum.production, 1);
    private final Factory factory = (Factory) BuildingEnum.factory.getBuildTypes();
    private static final String Q_TABLE_SAVE_PATH = "qtable.ser";
    private final TechTree tree;
    private int tickCounter = 0;
    private final AStarPathfinderXZ aStarPathfinderXZ;
    private final ProvinceManager provinceManager;
    private final QLearning qLearning = new QLearning();
    private boolean capped;

    public UnifiedAI(Country country, ClickWarSystem clickWarSystem) {
        aStarPathfinderXZ=country.getMilitary().getAStarPathfinder();
        this.country = country;
        this.clickWarSystem = clickWarSystem;
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveQTable));
        loadQTable();
        tree=ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
        provinceManager = ContinentalManagers.world(country.getInstance()).provinceManager();
        country.setAi(this);
    }

    @Override
    public void tick() {
        if (capped)return;
        GameState state = gatherGameState();
        String stateKey = state.toString();
        UnifiedAI.Action.ActionType actionType = qLearning.chooseAction(stateKey);

        ResearchOption researchOption = null;
        Country targetCountry = null;
        if (actionType == Action.ActionType.CONQUER){
            targetCountry = selectTargetCountry();
        }
        if (actionType == UnifiedAI.Action.ActionType.RESEARCH) {
            researchOption = selectResearchOption();
        }

        Action action = new Action(actionType, researchOption, targetCountry);
        double reward = executeAndGetReward(action);
        int activeWars = countActiveWars();
        if (activeWars > 1) {
            double warPenalty = -10 * activeWars;
            reward += warPenalty;
        }

        GameState newState = gatherGameState();
        if (country.getInfo().isCapitulated()){
            qLearning.updateQValue(stateKey, actionType, -1000, newState.toString());
            capped=true;
        }
        qLearning.updateQValue(stateKey, actionType, reward, newState.toString());
        tickCounter++;

        if (tickCounter % 100 == 0) {
            saveQTable();
        }
    }

    @Override
    public void fasterTick() {
        if (capped) return;

        if (shouldDefend()) {
            Action defendAction = new Action(Action.ActionType.DEFEND, null, null);
            double defendReward = executeAndGetReward(defendAction);
            GameState newState = gatherGameState();
            String stateKey = newState.toString();
            qLearning.updateQValue(stateKey, Action.ActionType.DEFEND, defendReward, newState.toString());
        }

        Action attackAction = null;
        Country targetCountry = selectTargetCountry();
        if (targetCountry != null && shouldAttack(targetCountry)) {
            if (r.nextBoolean()) {
                attackAction = new Action(Action.ActionType.START_WAR, null, targetCountry);
            } else {
                attackAction = new Action(Action.ActionType.CONQUER, null, targetCountry);
            }

            double attackReward = executeAndGetReward(attackAction);
            GameState newState = gatherGameState();
            String stateKey = newState.toString();
            qLearning.updateQValue(stateKey, attackAction.type(), attackReward, newState.toString());
        }
    }

    private ResearchOption selectResearchOption() {
        if (country.getResearch() == null) return null;

        ResearchCountry researchCountry = country.getResearch().researchCountry();
        List<String> availableOptions = tree.getAvailable(researchCountry);

        return tree.getResearchOption(availableOptions.stream().skip(r.nextInt(availableOptions.size())).findFirst().orElse(null));
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
            qLearning.setQTable((HashMap<String, HashMap<UnifiedAI.Action.ActionType, Double>>) in.readObject());
        } catch (IOException | ClassNotFoundException ignored) {
        }
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
            case CONQUER -> {
                Country target = action.targetCountry();
                if (target == null) {
                    yield -1;
                }

                boolean wasCapitulated = target.getInfo().isCapitulated();
                boolean success = attemptConquest(target);

                if (success) {
                    boolean nowCapitulated = target.getInfo().isCapitulated();
                    if (nowCapitulated && !wasCapitulated) {
                        yield 50;
                    }
                    yield 15;
                } else {
                    yield 0;
                }
            }

            case START_WAR -> {
                double warPenalty = 5;
                if (countActiveWars()>=1){
                    warPenalty = -50;
                }
                startWar();
                yield warPenalty;
            }

            case RESEARCH -> {
                startResearch(action.researchOption());
                if (country.getResearch()==null){
                    yield 0;
                }
                if (country.getResearch().researchCountry().isResearching()){
                    yield -2;
                }
                yield 2;
            }

            case BUILD_FACTORY -> {
                buildFactory(new ArrayList<>(country.getMilitary().getCities()));
                yield 1;
            }

            case BUILD_RESEARCH -> {
                buildResearch();
                yield 1;
            }

            case DEFEND -> {
                boolean success = defendTerritory();
                yield success ? 10 : -3;
            }
        };
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

    private void startResearch(ResearchOption researchOption) {
        if (country.getResearch()==null)return;
        ResearchCountry researchCountry = country.getResearch().researchCountry();
        if (researchOption.canResearch(researchCountry)) {
            researchCountry.startResearching(researchOption);
        }
    }
    Task task;

    private boolean attemptConquest(Country target) {
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


    private void startWar() {
        List<String> s = country.getMilitary().getBorders().stream().toList();
        if (s.isEmpty())return;
        String count = s.get(new Random().nextInt(0, s.size()));
        Country atk = ContinentalManagers.world(country.getInstance()).countryDataManager().getCountryFromName(count);
        if (!country.canFight(atk) || country.isAtWar(atk.getName()) || country.getDiplomacy().getWarJustification(count)!=null || country.getDiplomacy().getCompletedWarJustification(count)!=null) {
            return;
        }
        WarGoalType w = WarGoalTypeEnum.surprise.getWarGoalType();
        WarJustification warJustification = new WarJustification(country, atk, w.modifier(), w.timeToMake(), w.expires(), true);
        EventDispatcher.call(new WarJustificationStartEvent(warJustification, country));
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

    public record Action(UnifiedAI.Action.ActionType type, ResearchOption researchOption,
                         Country targetCountry) {
        public enum ActionType {
            BUILD_FACTORY,
            BUILD_RESEARCH,
            RESEARCH,
            START_WAR,
            CONQUER,
            DEFEND
        }
    }
}