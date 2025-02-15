package org.drachens.Manager.per_instance;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public Instance getInstance() {
        return instance;
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

    public List<String> getFactionNames() {
        return factionNames;
    }

    public List<Faction> getFactions() {
        return factions;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();

        JsonArray countryNamesJsonArray = new JsonArray();
        for (String countryName : countryNameList) {
            countryNamesJsonArray.add(new JsonPrimitive(countryName));
        }
        jsonObject.add("countries", countryNamesJsonArray);

        JsonArray factionNamesArray = new JsonArray();
        for (String countryName : factionNames) {
            factionNamesArray.add(new JsonPrimitive(countryName));
        }
        jsonObject.add("factions", factionNamesArray);

        JsonArray countriesJsonArray = new JsonArray();
        for (Country country : countries) {
            countriesJsonArray.add(country.toJson());
        }
        jsonObject.add("countries", countriesJsonArray);
        return jsonObject;
    }
}
