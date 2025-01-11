package org.drachens.temporary.clicks;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.VotingWinner;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.Research.tree.TechTree;
import org.drachens.events.NewDay;
import org.drachens.interfaces.AI;
import org.drachens.interfaces.AIManager;
import org.drachens.temporary.Factory;
import org.drachens.temporary.research.ResearchCountry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ClicksAI implements AIManager {
    private final VotingWinner votingWinner;
    private final HashMap<Country, AI> ais = new HashMap<>();

    public ClicksAI(VotingWinner votingWinner){
        this.votingWinner=votingWinner;
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e->{
            tick(e.getInstance());
        });
    }

    @Override
    public VotingWinner getIdentifier() {
        return votingWinner;
    }

    @Override
    public AI createAIForCountry(Country country) {
        AI ai = new ResearchGrinder((ResearchCountry) country);
        ais.put(country,ai);
        return ai;
    }

    @Override
    public void tick(Instance instance) {
        ais.forEach(((country, ai) -> ai.tick()));
    }

    static class FactorySpammer extends AI {
        private final Factory factory = (Factory) BuildingEnum.factory.getBuildTypes();
        private final Country country;
        private final Random r = new Random();
        private boolean canBuild;

        public FactorySpammer(Country country){
            this.country=country;
        }

        public void tick(){
            if (canBuild)return;
            int count = (int) (country.getVault().getAmount(CurrencyEnum.production.getCurrencyType())/5f);
            buildFactory(new ArrayList<>(country.getCities()),count);
        }

        private void buildFactory(List<Province> cities,int count){
            if (cities.isEmpty()){
                if (country.getVault().getAmount(CurrencyEnum.production.getCurrencyType())>10f){
                    canBuild=true;
                }
                return;
            }
            Province province = cities.get(r.nextInt(cities.size()));
            if (province.getBuilding()!=null){
                if (factory.requirementsToUpgrade(province.getBuilding(),country,1,null)){
                    factory.upgrade(1,province.getBuilding(),country,null);
                }
            }else {
                if (factory.canBuild(country, province, null)){
                    factory.forceBuild(country, province, null);
                }else{
                    cities.remove(province);
                    buildFactory(cities,count);
                }
            }
            count--;
            if (count>1){
                buildFactory(cities,count);
            }
        }
    }

    static class ResearchGrinder extends AI{
        private final List<Province> centers = new ArrayList<>();
        private final HashMap<Province,List<Province>> availableSpaces = new HashMap<>();
        private final ResearchCountry country;
        private final TechTree tree;
        private final Random r = new Random();
        private final ResearchCenter researchCenter = (ResearchCenter) BuildingEnum.researchCenter.getBuildTypes();
        public ResearchGrinder(ResearchCountry country){
            this.country=country;
            tree = ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
        }
        
        public void startResearch(){
            List<String> s = tree.getAvailable(country);
            String active = s.get(new Random().nextInt(s.size()));
            ResearchOption r = tree.getResearchOption(active);
            if (r.canResearch(country)){
                country.startResearching(r);
            }
        }

        public void buildResearch(){
            if (availableSpaces.isEmpty()){
                List<Province> provinces = country.getOccupies();
                Province province = provinces.get(r.nextInt(provinces.size()));
                if (researchCenter.canBuild(country, province, null)){
                    researchCenter.forceBuild(country, province, null);
                    availableSpaces.put(province,new ArrayList<>(province.getNeighbours()));
                    centers.add(province);
                }
            }else {
                Province p = centers.get(new Random().nextInt(centers.size()));
                List<Province> prov = availableSpaces.get(p);
                if (prov.isEmpty()){
                    availableSpaces.remove(p);
                    centers.remove(p);
                    return;
                }
                Province selected = prov.get(new Random().nextInt(prov.size()));
                switch (new Random().nextInt(3)){
                    case 0:
                        BuildTypes b = BuildingEnum.researchLab.getBuildTypes();
                        if (b.canBuild(country,selected,null)){
                            b.forceBuild(country,selected,null);
                        }
                        break;
                    case 1:
                        BuildTypes bc = BuildingEnum.library.getBuildTypes();
                        if (bc.canBuild(country,selected,null)){
                            bc.forceBuild(country,selected,null);
                        }
                        break;
                    case 2:
                        BuildTypes bd = BuildingEnum.university.getBuildTypes();
                        if (bd.canBuild(country,selected,null)){
                            bd.forceBuild(country,selected,null);
                        }
                        break;
                }
                prov.remove(selected);
                availableSpaces.put(p,prov);
            }
        }

        @Override
        public void tick() {
            buildResearch();
            if (!country.isResearching()){
                startResearch();
            }
        }
    }

    static class GlobalDominationAI extends AI {
        private final Country country;
        public GlobalDominationAI(Country country){
            this.country=country;
        }
        @Override
        public void tick() {

        }

        public void startAWar(){

        }

        public void conquerCountry(){
            
        }
    }
}
