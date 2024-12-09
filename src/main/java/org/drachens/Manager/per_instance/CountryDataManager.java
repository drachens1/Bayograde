package org.drachens.Manager.per_instance;

import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountryDataManager {
    private final List<String> countryNameList = new ArrayList<>();
    private final HashMap<String, Country> countryHashMap = new HashMap<>();
    private final Instance instance;
    private final HashMap<String, Factions> factionsHashMap = new HashMap<>();
    private final List<Factions> factions = new ArrayList<>();
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

    public void addFaction(Factions factions) {
        this.factions.add(factions);
        this.factionNames.add(factions.getStringName());
        factionsHashMap.put(factions.getStringName(), factions);
    }

    public void removeFaction(Factions factions) {
        this.factions.remove(factions);
        this.factionNames.remove(factions.getStringName());
        factionsHashMap.remove(factions.getStringName());
    }

    public boolean factionExists(String name) {
        return factionNames.contains(name);
    }

    public void renameFaction(String old, String newName, Factions faction) {
        factionNames.remove(old);
        factionNames.add(newName);
        factionsHashMap.remove(old);
        factionsHashMap.put(newName, faction);
    }

    public Factions getFaction(String name) {
        return factionsHashMap.get(name.toLowerCase());
    }

    public List<String> getFactionNames() {
        return factionNames;
    }

    public List<Factions> getFactions() {
        return factions;
    }
}
