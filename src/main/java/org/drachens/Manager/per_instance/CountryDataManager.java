package org.drachens.Manager.per_instance;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.bossbars.YearBar;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.dataClasses.VotingOption;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Setter
@Getter
public class CountryDataManager implements Saveable {
    private final List<String> countryNameList = new ArrayList<>();
    private final HashMap<String, Country> countryHashMap = new HashMap<>();
    private final Instance instance;
    private final HashMap<String, Faction> factionsHashMap = new HashMap<>();
    private final List<Faction> factions = new ArrayList<>();
    private final List<String> factionNames = new ArrayList<>();
    private List<Country> countries;

    public CountryDataManager(Instance instance, List<Country> countries) {
        this.instance = instance;
        this.countries = countries;
        for (Country country : countries) {
            countryNameList.add(country.getName());
            countryHashMap.put(country.getName(), country);
        }
    }

    public Country getCountryFromName(String name) {
        return countryHashMap.get(name);
    }

    public List<String> getNamesList() {
        return countryNameList;
    }

    public void addCountry(Country country) {
        countries.add(country);
        countryHashMap.put(country.getName(), country);
        countryNameList.add(country.getName());
    }

    public void addFaction(Faction faction) {
        this.factions.add(faction);
        this.factionNames.add(faction.getStringName());
        factionsHashMap.put(faction.getStringName(), faction);
    }

    public void removeFaction(Faction faction) {
        this.factions.remove(faction);
        this.factionNames.remove(faction.getStringName());
        factionsHashMap.remove(faction.getStringName());
    }

    public boolean factionExists(String name) {
        return factionNames.contains(name);
    }

    public void renameFaction(String old, String newName, Faction faction) {
        factionNames.remove(old);
        factionNames.add(newName);
        factionsHashMap.remove(old);
        factionsHashMap.put(newName, faction);
    }

    public Faction getFaction(String name) {
        return factionsHashMap.get(name.toLowerCase());
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();

        JsonArray factionNamesArray = new JsonArray();
        for (String countryName : factionNames) {
            factionNamesArray.add(new JsonPrimitive(countryName));
        }
        jsonObject.add("factions", factionNamesArray);

        JsonArray countriesJsonArray = new JsonArray();
        for (Country country : countries) {
            JsonObject jsonObject1 = new JsonObject();
            jsonObject1.add(country.getName(),country.toJson());
            countriesJsonArray.add(jsonObject1);
        }
        jsonObject.add("countries", countriesJsonArray);

        JsonArray provinces = new JsonArray();
        ContinentalManagers.world(instance).provinceManager().getProvinceHashMap().forEach((flatPos, province) -> provinces.add(province.getActualJson()));
        jsonObject.add("provinces",provinces);

        JsonObject extra = new JsonObject();

        YearBar yearBar = ContinentalManagers.yearManager.getYearBar(instance);
        extra.add("day",new JsonPrimitive(yearBar.getDay()));
        extra.add("month",new JsonPrimitive(yearBar.getMonth()));
        extra.add("year",new JsonPrimitive(yearBar.getYear()));
        jsonObject.add("extra",extra);

        VotingOption votingOption = ContinentalManagers.world(instance).dataStorer().votingOption;
        jsonObject.add("voting",votingOption.toJson());
        return jsonObject;
    }
}
