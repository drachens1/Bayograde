package org.drachens.generalGame.clicks;

import com.google.gson.JsonElement;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarGoalType;
import org.drachens.dataClasses.Diplomacy.Justifications.WarGoalTypeEnum;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.dataClasses.Research.ResearchCountry;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.events.NewDay;
import org.drachens.events.countries.warjustification.WarJustificationStartEvent;
import org.drachens.generalGame.factory.Factory;
import org.drachens.interfaces.AStarPathfinderVoids;
import org.drachens.interfaces.Saveable;
import org.drachens.interfaces.ai.AI;
import org.drachens.interfaces.ai.AIManager;
import org.drachens.util.AStarPathfinderXZ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.drachens.util.OtherUtil.runThread;

public class ClicksAI implements AIManager, Saveable {
    private final VotingWinner votingWinner;
    private final HashMap<Country, AI> ais = new HashMap<>();

    public ClicksAI(VotingWinner votingWinner) {
        this.votingWinner = votingWinner;
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e -> runThread(()->tick(e.world())));
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e -> runThread(()->fasterTick(e.world())));
    }

    @Override
    public VotingWinner getIdentifier() {
        return votingWinner;
    }

    @Override
    public AI createAIForCountry(Country country) {
        AI ai = new UnifiedAI(country,(ClickWarSystem) votingWinner.getVotingOption().getWar());
        ais.put(country, ai);
        return ai;
    }

    @Override
    public void tick(Instance instance) {
        ais.forEach((country, ai) -> {
            if (country.getInfo().getPlayers().isEmpty()) ai.tick();
        });
    }

    @Override
    public void fasterTick(Instance instance) {
        ais.forEach((country, ai) -> {
            if (country.getInfo().getPlayers().isEmpty()) ai.fasterTick();
        });
    }

    @Override
    public void removeAi(Country country) {
        ais.remove(country);
    }

    @Override
    public JsonElement toJson() {
        return null;
    }

    static class FactorySpammer extends AI {
        private final float industriosness = 100; // economist based of this
        private final Factory factory = (Factory) BuildingEnum.factory.getBuildTypes();
        private final Country country;
        private final Random r = new Random();
        private boolean canBuild;

        public FactorySpammer(Country country) {
            this.country = country;
        }

        @Override
        public void tick() {
            if (canBuild) return;
            int count = (int) (country.getEconomy().getVault().getAmount(CurrencyEnum.production.getCurrencyType()) / 5.0f);
            buildFactory(new ArrayList<>(country.getMilitary().getCities()), count);
        }

        @Override
        public void fasterTick() {

        }

        private void buildFactory(List<Province> cities, int count) {
            while (true) {
                if (cities.isEmpty()) {
                    if (10.0f < country.getEconomy().getVault().getAmount(CurrencyEnum.production.getCurrencyType()))
                        this.canBuild = true;
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
                    this.buildFactory(cities, count);
                }
                count--;
                if (1 < count) continue;
                return;
            }
        }
    }

    static class ResearchGrinder extends AI {
        private final float research = 100; // researchness based off this
        private final List<Province> centers = new ArrayList<>();
        private final HashMap<Province, List<Province>> availableSpaces = new HashMap<>();
        private final ResearchCountry researchCountry;
        private final Country country;
        private final TechTree tree;
        private final Random r = new Random();
        private final ResearchCenter researchCenter = (ResearchCenter) BuildingEnum.researchCenter.getBuildTypes();

        public ResearchGrinder(Country country) {
            this.country = country;
            this.researchCountry = country.getResearch().researchCountry();
            tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
        }

        public void startResearch() {
            List<String> s = tree.getAvailable(researchCountry);
            String active = s.get(new Random().nextInt(s.size()));
            ResearchOption r = tree.getResearchOption(active);
            if (r.canResearch(researchCountry)) {
                researchCountry.startResearching(r);
            }
        }

        public void buildResearch() {
            if (availableSpaces.isEmpty()) {
                List<Province> provinces = country.getMilitary().getOccupies();
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

        @Override
        public void tick() {
            buildResearch();
            if (!researchCountry.isResearching()) {
                startResearch();
            }
        }

        @Override
        public void fasterTick() {

        }
    }

    static class GlobalDominationAI extends AI {
        private final float aggression = 100; //aggressiveness based off this
        private final Payment payment = new Payment(CurrencyEnum.production, 1);
        private final Country country;
        private final ClickWarSystem clickWarSystem;
        private final AStarPathfinderXZ aStarPathfinderXZ;

        public GlobalDominationAI(Country country, ClickWarSystem clickWarSystem) {
            this.clickWarSystem = clickWarSystem;
            this.country = country;
            this.aStarPathfinderXZ=country.getMilitary().getAStarPathfinder();
        }

        @Override
        public void tick() {
            if (!country.getMilitary().getBordersWars().isEmpty()) {
                conquerCountry(new ArrayList<>(country.getMilitary().getBordersWars()));
            } else if (country.getDiplomacy().getWarJustificationCountries().isEmpty()) {
                startAWar();
            }
        }

        @Override
        public void fasterTick() {

        }

        public void startAWar() {
            List<String> s = country.getMilitary().getBorders().stream().toList();
            String count = s.get(new Random().nextInt(0,s.size()));
            Country atk = ContinentalManagers.world(country.getInstance()).countryDataManager().getCountryFromName(count);
            if (!country.canFight(atk) || country.isAtWar(atk.getName())) {
                return;
            }
            WarGoalType w = WarGoalTypeEnum.surprise.getWarGoalType();
            WarJustification warJustification = new WarJustification(atk, country, w.modifier(), w.timeToMake(), w.expires(), true);
            EventDispatcher.call(new WarJustificationStartEvent(warJustification, country));
        }

        public boolean captureProvince(Province province){
            if (clickWarSystem.canCapture(province,country)!=null){
                province.setOccupier(country);
                country.removePayment(payment);
                return true;
            }
            return false;
        }

        public void conquerCountry(List<String> s) {
            while (true) {
                final Country atk = ContinentalManagers.world(this.country.getInstance()).countryDataManager().getCountryFromName(s.getFirst());
                List<Province> citiesToCapture = atk.getMilitary().getCities();
                Province capital = atk.getInfo().getCapital();
                capturePath(aStarPathfinderXZ.findPath(country.getInfo().getCapital(),capital,country,new Pather()));
                if (atk.getInfo().isCapitulated()){
                    return;
                }
            }
        }

        public void capturePath(List<AStarPathfinderXZ.Node> provinces){
            for (AStarPathfinderXZ.Node node : provinces){
                Province province = node.province;
                province.getNeighbours().forEach(neighbour->{
                    if (country.canFight(neighbour.getOccupier())){
                        captureProvince(neighbour);
                    }
                });
                if (!captureProvince(province)){
                    break;
                }
            }
        }
    }


    static class Pather implements AStarPathfinderVoids {

        @Override
        public boolean isWalkable(Province check, Country country) {
            return true;
        }
    }
}
