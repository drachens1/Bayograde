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
import org.drachens.events.countries.war.StartWarEvent;
import org.drachens.events.countries.warjustification.WarJustificationStartEvent;
import org.drachens.generalGame.factory.Factory;
import org.drachens.interfaces.AI;
import org.drachens.interfaces.AIManager;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class ClicksAI implements AIManager, Saveable {
    private final VotingWinner votingWinner;
    private final HashMap<Country, AI> ais = new HashMap<>();

    public ClicksAI(VotingWinner votingWinner) {
        this.votingWinner = votingWinner;
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e -> tick(e.world()));
    }

    @Override
    public VotingWinner getIdentifier() {
        return votingWinner;
    }

    @Override
    public AI createAIForCountry(Country country) {
        AI ai = new GlobalDominationAI(country,(ClickWarSystem) votingWinner.getVotingOption().getWar());
        ais.put(country, ai);
        return ai;
    }

    @Override
    public void tick(Instance instance) {
        ais.forEach((country, ai) -> ai.tick());
    }

    @Override
    public JsonElement toJson() {
        return null;
    }

    static class FactorySpammer extends AI {
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
    }

    static class GlobalDominationAI extends AI {
        private final float aggression = 100;
        private final Payment payment = new Payment(CurrencyEnum.production, 1);
        private final Country country;
        private final Consumer<WarJustification> c;
        private final ClickWarSystem clickWarSystem;

        public GlobalDominationAI(Country country, ClickWarSystem clickWarSystem) {
            this.clickWarSystem = clickWarSystem;
            this.country = country;
            c = warJust -> EventDispatcher.call(new StartWarEvent(country, warJust.against(), warJust));
        }

        @Override
        public void tick() {
            if (!country.getMilitary().getBordersWars().isEmpty()) {
                conquerCountry(new ArrayList<>(country.getMilitary().getBordersWars()));
            } else if (country.getDiplomacy().getWarJustificationCountries().isEmpty()) {
                startAWar();
            }
        }

        public void startAWar() {
            List<String> s = country.getMilitary().getBorders().stream().toList();
            String count = s.get(new Random().nextInt(0,s.size()));
            Country atk = ContinentalManagers.world(country.getInstance()).countryDataManager().getCountryFromName(count);
            if (!country.canFight(atk) || country.isAtWar(atk.getName())) {
                return;
            }
            WarGoalType w = WarGoalTypeEnum.surprise.getWarGoalType();
            WarJustification warJustification = new WarJustification(atk, country, w.modifier(), w.timeToMake(), w.expires(), c);
            EventDispatcher.call(new WarJustificationStartEvent(warJustification, country));
        }

        public void conquerCountry(List<String> s) {
            while (true) {
                final Country atk = ContinentalManagers.world(this.country.getInstance()).countryDataManager().getCountryFromName(s.getFirst());
                final List<Province> provinces = this.country.getMilitary().getBorder(s.getFirst());
                if (null == provinces) {
                    s.remove(atk.getName());
                    if (s.isEmpty()) {
                        return;
                    }

                    continue;
                }

                for (final Province province : new ArrayList<>(provinces)) {
                    final Province from = this.clickWarSystem.canCapture(province, this.country);
                    if (null != from) {
                        this.country.removePayment(this.payment);
                        province.capture(from.getOccupier());
                    }
                    return;
                }
                s.remove(atk.getName());
                if (s.isEmpty()) {
                    return;
                }

            }
        }
    }
}
