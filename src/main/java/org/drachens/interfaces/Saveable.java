package org.drachens.interfaces;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.drachens.Manager.defaults.ContinentalManagers;

public interface Saveable {
    Gson gson = ContinentalManagers.saveManager.getGson();

    JsonElement toJson();
}
