package org.drachens.temporary.clicks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.dataClasses.territories.Province;
import org.drachens.interfaces.AI;
import org.drachens.interfaces.AIManager;
import org.drachens.temporary.Factory;

import net.minestom.server.instance.Instance;

public class ClicksAI implements AIManager {

    @Override
    public AI createAIForCountry(Country country) {
        return null;
    }

    @Override
    public void tick(Instance instance) {

    }

    static class FactorySpammer extends AI {
        private final Factory factory = (Factory) BuildingEnum.factory.getBuildTypes();
        private final Country country;
        private final Random r = new Random();

        public FactorySpammer(Country country){
            this.country=country;
        }

        public void tick(){
            buildFactory(country.getCities());
        }

        private void buildFactory(List<Province> cities){
            Province province = cities.get(r.nextInt(cities.size()));
            if (factory.canBuild(country, province, null)){
                factory.forceBuild(country, province, null);
            }else{
                cities.remove(province);
                buildFactory(cities);
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
