package org.drachens.Manager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.player_types.CPlayer;

@Getter
public class SaveManager {
    private final Gson gson = new GsonBuilder()
            .create();

    public void load(String name, CPlayer player){

    }

    public void save(Instance instance){
        CountryDataManager countryDataManager = ContinentalManagers.world(instance).countryDataManager();
    }

}
