package org.drachens.Manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.serializer.SerializerModule;
import org.drachens.player_types.CPlayer;

public class SaveManager {
    private final Gson gson = new GsonBuilder()
            .create();

    public void load(String name, CPlayer player){

    }

    public void save(Instance instance){
        CountryDataManager countryDataManager = ContinentalManagers.world(instance).countryDataManager();
        try {
            System.out.println(new ObjectMapper().registerModule(new SerializerModule()).disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).writeValueAsString(countryDataManager));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Gson getGson(){
        return gson;
    }
}
