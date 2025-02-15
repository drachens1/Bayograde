package org.drachens.dataClasses.customgame;

import com.google.gson.JsonElement;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.dataClasses.datastorage.DataStorer;
import org.drachens.dataClasses.datastorage.WorldClasses;
import org.drachens.dataClasses.other.ClientEntsToLoad;

public class CustomGameWorldClass extends WorldClasses {
    public CustomGameWorldClass(CountryDataManager countryDataManager, ClientEntsToLoad clientEntsToLoad, ProvinceManager provinceManager, DataStorer dataStorer) {
        super(countryDataManager, clientEntsToLoad, provinceManager, dataStorer);
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
