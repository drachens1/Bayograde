package org.drachens.dataClasses.datastorage;

import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.additional.GlobalGameWorldClass;
import org.drachens.dataClasses.customgame.CustomGameWorldClass;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.interfaces.Saveable;

public abstract class WorldClasses implements Saveable {
    private final CountryDataManager countryDataManager;
    private final ClientEntsToLoad clientEntsToLoad;
    private final ProvinceManager provinceManager;
    private final DataStorer dataStorer;

    protected WorldClasses(CountryDataManager countryDataManager, ClientEntsToLoad clientEntsToLoad, ProvinceManager provinceManager, DataStorer dataStorer){
        this.countryDataManager=countryDataManager;
        this.clientEntsToLoad=clientEntsToLoad;
        this.provinceManager=provinceManager;
        this.dataStorer=dataStorer;
    }

    public CountryDataManager countryDataManager(){
        return countryDataManager;
    }

    public ClientEntsToLoad clientEntsToLoad(){
        return clientEntsToLoad;
    }

    public ProvinceManager provinceManager(){
        return provinceManager;
    }

    public DataStorer dataStorer(){
        return dataStorer;
    }

    public boolean isGlobalGameWorldClass(){
        return this instanceof GlobalGameWorldClass;
    }

    public GlobalGameWorldClass getAsGlobalGameWorldClass(){
        return (GlobalGameWorldClass) this;
    }

    public boolean isCustomGameWorldClass(){
        return this instanceof CustomGameWorldClass;
    }

    public CustomGameWorldClass getAsCustomGameWorldClass(){
        return (CustomGameWorldClass) this;
    }
}
