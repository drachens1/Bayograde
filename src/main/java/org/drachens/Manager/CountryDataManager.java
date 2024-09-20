package org.drachens.Manager;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamManager;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.IdeologyTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class CountryDataManager {
    private final List<String> countryNameList = new ArrayList<>();
    private final List<IdeologyTypes> ideologyTypes;
    private final Ideology defaultIdeology;
    private final HashMap<String, Country> countryHashMap = new HashMap<>();
    private List<Country> countries;
    private Team defaultTeam;
    private final Instance instance;
    private YearManager yearManager;
    private Country superPower;

    public CountryDataManager(Instance instance, List<Country> countries, List<IdeologyTypes> ideologyTypes, Ideology defaultIdeology) {
        this.instance = instance;
        this.countries = countries;
        this.ideologyTypes = ideologyTypes;
        this.defaultIdeology = defaultIdeology;
        for (Country country : countries) {
            countryNameList.add(country.getName());
            countryHashMap.put(country.getName(), country);
        }
        TeamManager teamManager = MinecraftServer.getTeamManager();
        defaultTeam = teamManager.createBuilder("spectator")
                .prefix(compBuild("Spectator ", NamedTextColor.GRAY, TextDecoration.BOLD))
                .teamColor(NamedTextColor.GRAY)
                .build();
    }

    public Team getDefaultTeam() {
        return defaultTeam;
    }

    public void setDefaultTeam(Team defaultTeam) {
        this.defaultTeam = defaultTeam;
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

    public YearManager getYearManager() {
        return yearManager;
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

    public void reloadCountryNames() {
        countryHashMap.clear();
        countryNameList.clear();
        for (Country country : countries) {
            countryNameList.add(country.getName());
            countryHashMap.put(country.getName(), country);
        }
    }

    public void setSuperPower(Country superPower) {
        this.superPower = superPower;
    }

    public Country getSuperPower() {
        return superPower;
    }
    public Ideology getDefaultIdeology(){
        return defaultIdeology;
    }
    public List<IdeologyTypes> getIdeologyTypes(){
        return ideologyTypes;
    }
    public IdeologyTypes getIdeologyType(int index){
        return ideologyTypes.get(index);
    }
}
