package org.drachens.dataClasses.Countries;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.item.Material;
import net.minestom.server.scoreboard.Team;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.factory.PlaceableFactory;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.events.CountryChangeEvent;
import org.drachens.events.CountryJoinEvent;
import org.drachens.events.CountryLeaveEvent;

import java.util.*;

import static org.drachens.util.KyoriUtil.*;
import static org.drachens.util.Messages.broadcast;

public class Country extends Team {
    private final List<Player> players;
    private List<Province> cores;
    private List<Province> occupies;
    private List<Province> cities;
    private final HashMap<CurrencyTypes, Currencies> currenciesMap;
    private String name;
    private Material block;
    private Material border;
    private Province capital;
    private float capitulationPoints;
    private float maxCapitulationPoints;
    private final List<Material> city = new ArrayList<>();
    private final List<PlaceableFactory> placeableFactories = new ArrayList<>();
    public Country(HashMap<CurrencyTypes,Currencies> startingCurrencies, String name, Material block, Material border){
        super(name);
        this.cores = new ArrayList<>();
        this.occupies = new ArrayList<>();
        this.currenciesMap = startingCurrencies;
        this.name = name;
        this.setPrefix(compBuild(name, NamedTextColor.BLUE));
        this.block = block;
        this.border = border;
        this.players = new ArrayList<>();
        this.cities = new ArrayList<>();
        Material[] tempCities = {Material.CYAN_GLAZED_TERRACOTTA,Material.GREEN_GLAZED_TERRACOTTA,Material.LIME_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA,Material.RAW_GOLD_BLOCK,Material.GOLD_BLOCK,Material.EMERALD_BLOCK};
        city.addAll(Arrays.stream(tempCities).toList());
    }
    public List<Province> getCores(){
        return cores;
    }
    public void addCore(Province prov){
        cores.add(prov);
        prov.addCore(this);
    }
    public void removeCore(Province prov){
        cores.remove(prov);
        prov.removeCore(this);
    }
    public void calculateCapitulationPercentage(){
        float points = 0;
        for (Province city : cities){
            points+=this.city.indexOf(city.getMaterial());
        }
        maxCapitulationPoints = points;
        capitulationPoints = points;
    }
    public List<Province> getCities() {
        return cities;
    }
    public void setCities(List<Province> cities) {
        this.cities = cities;
    }
    public void addCity(Province city){
        this.cities.add(city);
    }
    public void removeCity(Province city){
        this.cities.remove(city);
    }
    public float getCapitulationPoints() {
        return capitulationPoints;
    }
    public float getMaxCapitulationPoints() {
        return maxCapitulationPoints;
    }
    public List<Province> getOccupies(){
        return occupies;
    }
    public void addOccupied(Province province){
        occupies.add(province);
    }
    public void removeOccupied(Province province){
        occupies.remove(province);
    }
    public void  setOccupies(List<Province> provinces){
        occupies = provinces;
    }
    public void setCores(List<Province> cores) {
        this.cores = cores;
    }
    public void add(CurrencyTypes currencyTypes, float amount){
        currenciesMap.get(currencyTypes).add(amount);
    }

    public HashMap<CurrencyTypes, Currencies> getCurrenciesMap() {
        return currenciesMap;
    }

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Material getBlock(){
        return block;
    }
    public void setBlock(Material block) {
        this.block = block;
    }
    public Material getBorder(){
        return border;
    }
    public void setBorder(Material border){
        this.border = border;
    }
    public Province getCapital(){
        return capital;
    }
    public void setCapital(Province capital) {
        this.capital = capital;
    }
    public void addPlayer(Player p){
        EventDispatcher.call(new CountryJoinEvent(this,p));
        this.players.add(p);
        p.sendMessage(mergeComp(getPrefixes("country"),replaceString(getCountryMessages("countryJoin"),"%country%",this.name)));
        broadcast(mergeComp(getPrefixes("country"),replaceString(replaceString(getCountryMessages("broadcastedCountryJoin"),"%country%",this.name),"%player%",p.getUsername())),p.getInstance());
        p.teleport(capital.getPos().add(0,1,0));
        p.setTeam(this);
        this.addMember(p.getUsername());
    }
    public void removePlayer(Player p, boolean left){
        if (left)EventDispatcher.call(new CountryLeaveEvent(this,p));
        this.players.remove(p);
        p.sendMessage(mergeComp(getPrefixes("country"),replaceString(getCountryMessages("countryLeave"),"%country%",this.name)));
    }

    public void changeCountry(Player p) {
        Team from = p.getTeam();

        if (from instanceof Country country) {
            if (country.getMembers().contains(p.getUsername())) {
                country.removePlayer(p,false);
            }
            EventDispatcher.call(new CountryChangeEvent(this,country,p));
        }

        addPlayer(p);
    }

    public List<Player> getPlayer(){
        return players;
    }
    public void cityCaptured(Province capturedCity){
    }

    public void addPlaceableFactory(PlaceableFactory placeableFactory){
        placeableFactories.add(placeableFactory);
    }
    public void removePlaceableFactory(PlaceableFactory placeableFactory){
        placeableFactories.remove(placeableFactory);
    }
    public List<PlaceableFactory> getPlaceableFactories(){
        return placeableFactories;
    }
    public void calculateIncrease(){
        HashMap<CurrencyTypes, Float> additionAmount = new HashMap<>();
        for (PlaceableFactory placeableFactory : placeableFactories){
            HashMap<CurrencyTypes, Float> newAdd = placeableFactory.onGenerate();
            for (Map.Entry<CurrencyTypes, Float> e : newAdd.entrySet()){
                if (additionAmount.containsKey(e.getKey())){
                    float current = additionAmount.get(e.getKey());
                    current+=e.getValue();
                    additionAmount.put(e.getKey(),current);
                }else {
                    additionAmount.put(e.getKey(),e.getValue());
                }
            }
        }
    }
}
