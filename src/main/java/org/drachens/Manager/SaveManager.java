package org.drachens.Manager;

import com.google.gson.*;
import lombok.Getter;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.dataClasses.serializers.CompletableFutureReflectionSerializer;
import org.drachens.dataClasses.serializers.EnumSerializer;
import org.drachens.dataClasses.serializers.MaterialSerializer;
import org.drachens.dataClasses.serializers.SaveableSerializer;
import org.drachens.interfaces.Saveable;
import org.drachens.player_types.CPlayer;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.drachens.util.OtherUtil.runThread;

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
        try (FileWriter writer = new FileWriter("6b0e83c3-d08a-4243-adf6-3bf89bddd542.json")) {
            gson.toJson(jsonElement, writer);
            System.out.println("Successfully wrote JSON to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(String name, CPlayer player){
        try (FileReader reader = new FileReader("6b0e83c3-d08a-4243-adf6-3bf89bddd542.json")) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            runThread(()->load(player.getInstance(),jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(Instance instance, JsonObject jsonObject){
        //CountryDataManager countryDataManager = new CountryDataManager(instance);
        if (ContinentalManagers.yearManager.contains(instance)) {
            ContinentalManagers.yearManager.getYearBar(instance).cancelTask();
        }
        VotingWinner v = VotingWinner.valueOf(jsonObject.getAsJsonObject("voting").get("name").getAsString());
        JsonArray countries = jsonObject.getAsJsonArray("countries");
        List<Country> counts = new ArrayList<>();
        System.out.println(countries.get(0));
        countries.forEach(jsonElement -> {
//            Country country = switch (v) {
//                case ww2_clicks -> new ClicksCountry(newCurrencies, countryName.identifier(), countryName.name(), countryName.block(), countryName.border(), defIdeology, instance, laws);
//                case ww2_troops -> new TroopCountry(newCurrencies, countryName.identifier(), countryName.name(), countryName.block(), countryName.border(), defIdeology, instance, laws);
//                default -> throw new IllegalArgumentException("Unexpected voting winner");
//            };
        });
    }
}
