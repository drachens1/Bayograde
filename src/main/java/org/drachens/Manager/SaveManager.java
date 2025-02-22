package org.drachens.Manager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.additional.EventsRunner;
import org.drachens.dataClasses.additional.IdeologyGain;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.additional.greatDepression.GreatDepressionEventsRunner;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.dataClasses.serializers.*;
import org.drachens.interfaces.Saveable;
import org.drachens.player_types.CPlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class SaveManager {
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Saveable.class,new SaveableSerializer())
            .registerTypeAdapter(Modifier.class, new SaveableSerializer())
            .registerTypeAdapter(LawCategory.class, new SaveableSerializer())
            .registerTypeAdapter(Province.class, new SaveableSerializer())
            .registerTypeAdapter(Material.class,new MaterialSerializer())
            .registerTypeAdapter(Enum.class,new EnumSerializer())
            .registerTypeAdapter(CompletableFuture.class,new CompletableFutureReflectionSerializer())
            .create();


    public void save(Instance instance) {
        CountryDataManager countryDataManager = ContinentalManagers.world(instance).countryDataManager();
        JsonElement jsonElement = countryDataManager.toJson();
        System.out.println(jsonElement);
        Gson gson = new GsonBuilder().create();
        try (FileWriter writer = new FileWriter(UUID.randomUUID().toString()+".txt")) {
            gson.toJson(jsonElement, writer);
            System.out.println("Successfully wrote JSON to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String name, CPlayer player){

    }
}
