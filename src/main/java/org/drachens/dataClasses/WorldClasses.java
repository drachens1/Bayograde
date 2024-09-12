package org.drachens.dataClasses;

import org.drachens.Manager.CountryDataManager;
import org.drachens.Manager.MapGeneratorManager;
import org.drachens.Manager.YearManager;

public class WorldClasses {
    private final YearManager yearManager;
    private final CountryDataManager countryDataManager;
    private final MapGeneratorManager mapGeneratorManager;
    public WorldClasses(YearManager yearManager,CountryDataManager countryDataManager,MapGeneratorManager mapGeneratorManager){
        this.yearManager = yearManager;
        this.countryDataManager = countryDataManager;
        this.mapGeneratorManager = mapGeneratorManager;
    }
    public YearManager getYearManager() {
        return yearManager;
    }
    public CountryDataManager getCountryDataManager() {
        return countryDataManager;
    }
    public MapGeneratorManager getMapGeneratorManager(){
        return mapGeneratorManager;
    }
}
