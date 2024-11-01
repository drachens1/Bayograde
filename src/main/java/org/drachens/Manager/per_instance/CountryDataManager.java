package org.drachens.Manager.per_instance;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.scoreboard.Team;
import net.minestom.server.scoreboard.TeamManager;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Diplomacy.faction.Factions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class CountryDataManager {
    private final List<String> countryNameList = new ArrayList<>();
    private final HashMap<String, Country> countryHashMap = new HashMap<>();
    private List<Country> countries;
    private Team defaultTeam;
    private final Instance instance;
    private Country superPower;
    private final HashMap<IdeologyTypes,List<Leader>> unusedLeaders = new HashMap<>();
    private final HashMap<String, Factions> factionsHashMap = new HashMap<>();
    private List<Factions> factions = new ArrayList<>();
    private List<String> factionNames = new ArrayList<>();
    public CountryDataManager(Instance instance, List<Country> countries) {
        this.instance = instance;
        this.countries = countries;
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
    public void setSuperPower(Country superPower) {
        this.superPower = superPower;
    }

    public Country getSuperPower() {
        return superPower;
    }
    public void setUnusedLeaders(IdeologyTypes ideologyTypes,List<Leader> leaders){
        this.unusedLeaders.put(ideologyTypes,leaders);
    }
    public void addLeader(IdeologyTypes ideologyTypes,Leader leader){
        this.unusedLeaders.get(ideologyTypes).add(leader);
    }
    public void removeLeader(IdeologyTypes ideologyTypes,Leader leader){
        this.unusedLeaders.get(ideologyTypes).remove(leader);
    }
    public List<Leader> getUnusedLeaders(IdeologyTypes ideologyTypes){
        return unusedLeaders.get(ideologyTypes);
    }
    public void addFaction(Factions factions){
        this.factions.add(factions);
        this.factionNames.add(factions.getStringName());
        factionsHashMap.put(factions.getStringName(), factions);
    }
    public void removeFaction(Factions factions){
        this.factions.remove(factions);
        this.factionNames.remove(factions.getStringName());
        factionsHashMap.remove(factions.getStringName());
    }
    public Factions getFaction(String name){
        return factionsHashMap.get(name.toLowerCase());
    }
    public List<String> getFactionNames(){
        return factionNames;
    }
    public List<Factions> getFactions(){
        return factions;
    }
}
