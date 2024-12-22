package org.drachens.temporary.clicks;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.dataClasses.territories.Province;
import org.drachens.events.NewDay;
import org.drachens.interfaces.AI;
import org.drachens.interfaces.AIManager;
import org.drachens.temporary.Factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ClicksAI implements AIManager {
    private final HashMap<Country, AI> ais = new HashMap<>();

    public ClicksAI(){
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e->{
            tick(e.getInstance());
        });
    }
    @Override
    public AI createAIForCountry(Country country) {
        AI ai = new FactorySpammer(country);
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

        public FactorySpammer(Country country){
            this.country=country;
        }

        public void tick(){
            buildFactory(new ArrayList<>(country.getCities()));
        }

        private void buildFactory(List<Province> cities){
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
                    buildFactory(cities);
                }
            }

        }
    }

    static class ResearchGrinder {
        private final HashMap<Province,List<Province>> availableSpaces = new HashMap<>();
        private final Country country;
        private final Random r = new Random();
        private final ResearchCenter researchCenter = (ResearchCenter) BuildingEnum.researchCenter.getBuildTypes();
        public ResearchGrinder(Country country){
            this.country=country;
        }
        
        public void startResearch(){
            ContinentalManagers.world(country.getInstance()).dataStorer().votingOption.getTree();
        }

        public void buildResearch(){
            if (availableSpaces.isEmpty()){
                List<Province> provinces = country.getOccupies();
                Province province = provinces.get(r.nextInt(provinces.size()));
                if (researchCenter.canBuild(country, province, null)){
                    researchCenter.forceBuild(country, province, null);
                }
            }
        }
    }

    static class GlobalDominationAI {

    }


}
