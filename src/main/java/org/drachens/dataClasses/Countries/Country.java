package org.drachens.dataClasses.Countries;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyBoost;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.factory.PlaceableFactory;
import org.drachens.dataClasses.Modifier;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.territories.Region;
import org.drachens.events.Countries.CountryChangeEvent;
import org.drachens.events.Countries.CountryJoinEvent;
import org.drachens.events.Countries.CountryLeaveEvent;

import java.util.*;

import static org.drachens.util.KyoriUtil.*;
import static org.drachens.util.Messages.broadcast;
import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class Country implements Cloneable{
    private Leader leader;
    private final List<Player> players;
    private final HashMap<CurrencyTypes, Currencies> currenciesMap;
    private final List<Material> city = new ArrayList<>();
    private final List<PlaceableFactory> placeableFactories = new ArrayList<>();
    private List<Province> cores;
    private List<Province> occupies;
    private List<Province> cities;
    private String name;
    private Component nameComponent;
    private Material block;
    private Material border;
    private Province capital;
    private float capitulationPoints;
    private float maxCapitulationPoints;
    private boolean capitulated = false;
    private final List<Country> wars = new ArrayList<>();
    private CountryEnums.Type type;
    private CountryEnums.RelationsStyle relationsStyle;
    private CountryEnums.History history;
    private CountryEnums.Focuses focuses;
    private CountryEnums.PreviousWar previousWar;
    private final Ideology ideology;
    private final Election elections;
    private Component prefix;
    private Component description;
    private float maxBoost = 1f;
    private float capitulationBoostPercentage = 1f;
    private float stabilityBaseBoost = 0f;
    private float stabilityGainBoost = 0f;
    private final List<Region> region = new ArrayList<>();
    private final List<Modifier> modifiers = new ArrayList<>();
    private final HashMap<CurrencyTypes, Float> economyBoosts = new HashMap<>();
    private Country overlord = null;
    private List<Country> puppets = new ArrayList<>();
    public Country(HashMap<CurrencyTypes, Currencies> startingCurrencies, String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Election election) {
        this.cores = new ArrayList<>();
        this.occupies = new ArrayList<>();
        this.currenciesMap = startingCurrencies;
        this.nameComponent = nameComponent;
        this.name = name;
        this.setPrefix(compBuild(name, NamedTextColor.BLUE));
        this.block = block;
        this.border = border;
        this.players = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.ideology = defaultIdeologies.clone(this);
        this.elections = election;
        Material[] tempCities = {Material.CYAN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA, Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK};
        city.addAll(Arrays.stream(tempCities).toList());
    }
    public void addModifier(Modifier modifier){
        modifiers.add(modifier);
        this.maxBoost+=modifier.getMaxBoost();
        this.capitulationBoostPercentage+=modifier.getCapitulationBoostPercentage();
        this.stabilityBaseBoost+=modifier.getStabilityBaseBoost();
        this.stabilityGainBoost+=modifier.getStabilityGainBoost();
        modifier.getCurrencyBoostList().forEach(this::addBoost);
    }

    public void removeModifier(Modifier modifier){
        modifiers.remove(modifier);
        this.maxBoost-=modifier.getMaxBoost();
        this.capitulationBoostPercentage-=modifier.getCapitulationBoostPercentage();
        this.stabilityBaseBoost-=modifier.getStabilityBaseBoost();
        this.stabilityGainBoost-=modifier.getStabilityGainBoost();
        modifier.getCurrencyBoostList().forEach(this::removeBoost);
    }

    public List<Modifier> getModifiers(){
        return modifiers;
    }

    public boolean hasModifier(Modifier modifier){
        return modifiers.contains(modifier);
    }

    public List<Province> getCores() {
        return cores;
    }

    public void setCores(List<Province> cores) {
        this.cores = cores;
    }

    public void addCore(Province prov) {
        cores.add(prov);
        prov.addCore(this);
    }

    public void removeCore(Province prov) {
        cores.remove(prov);
        prov.removeCore(this);
    }

    public void calculateCapitulationPercentage() {
        float points = 0;
        for (Province city : cities) {
            points += this.city.indexOf(city.getMaterial());
        }
        maxCapitulationPoints = points;
        capitulationPoints = 0;
    }

    public List<Province> getCities() {
        return cities;
    }

    public void setCities(List<Province> cities) {
        this.cities = cities;
    }

    public void addCity(Province city) {
        this.maxCapitulationPoints+=this.city.indexOf(city.getMaterial());
        this.capitulationPoints+=this.city.indexOf(city.getMaterial());
        this.cities.add(city);
    }
    public void removeCity(Province city) {
        System.out.println(capitulationPoints+" + "+this.city.indexOf(city.getMaterial()));
        this.capitulationPoints+=this.city.indexOf(city.getMaterial());
        this.cities.remove(city);
    }

    public float getCapitulationPoints() {
        return capitulationPoints;
    }

    public float getMaxCapitulationPoints() {
        return maxCapitulationPoints;
    }

    public List<Province> getOccupies() {
        return occupies;
    }

    public void setOccupies(List<Province> provinces) {
        occupies = provinces;
    }

    public void addOccupied(Province province) {
        occupies.add(province);
    }

    public void removeOccupied(Province province) {
        occupies.remove(province);
    }

    public void add(CurrencyTypes currencyTypes, float amount) {
        currenciesMap.get(currencyTypes).add(amount);
    }

    public HashMap<CurrencyTypes, Currencies> getCurrenciesMap() {
        return currenciesMap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Component getNameComponent() {
        return nameComponent;
    }
    public void setName(Component name) {
        this.nameComponent = name;
    }
    public Material getBlock() {
        return block;
    }

    public void setBlock(Material block) {
        this.block = block;
    }

    public Material getBorder() {
        return border;
    }

    public void setBorder(Material border) {
        this.border = border;
    }

    public Province getCapital() {
        return capital;
    }

    public void setCapital(Province capital) {
        this.capital = capital;
    }

    public void addPlayer(Player p) {
        EventDispatcher.call(new CountryJoinEvent(this, p));
        this.players.add(p);
        p.sendMessage(mergeComp(getPrefixes("country"), replaceString(getCountryMessages("countryJoin"), "%country%", this.name)));
        broadcast(mergeComp(getPrefixes("country"), replaceString(replaceString(getCountryMessages("broadcastedCountryJoin"), "%country%", this.name), "%player%", p.getUsername())), p.getInstance());
        p.teleport(capital.getPos().add(0, 1, 0));
    }

    public void removePlayer(Player p, boolean left) {
        if (left) EventDispatcher.call(new CountryLeaveEvent(this, p));
        this.players.remove(p);
        p.sendMessage(mergeComp(getPrefixes("country"), replaceString(getCountryMessages("countryLeave"), "%country%", this.name)));
    }

    public void changeCountry(Player p) {
        Country country = getCountryFromPlayer(p);

        if (country != null){
            if (country.getPlayer().contains(p)) {
                country.removePlayer(p, false);
            }
            EventDispatcher.call(new CountryChangeEvent(this, country, p));
        }


        addPlayer(p);
    }

    public List<Player> getPlayer() {
        return players;
    }

    public void cityCaptured(Country attacker,Province capturedCity) {
        removeCity(capturedCity);
        if (!capitulated){
            if (capital==capturedCity){
                broadcast(mergeComp(getPrefixes("country"),compBuild(attacker.name+" has seized the "+name+" capital",NamedTextColor.RED)), capital.getInstance());
            }
        }
        System.out.println(capitulationPoints+" : "+maxCapitulationPoints*(0.8*capitulationBoostPercentage)+" : "+0.8*capitulationBoostPercentage);
        if (capitulationPoints >= maxCapitulationPoints*(0.8*capitulationBoostPercentage) && !capitulated){
            capitulated = true;
            capitulate(attacker);
        }
    }

    public void addPlaceableFactory(PlaceableFactory placeableFactory) {
        placeableFactories.add(placeableFactory);
    }

    public void removePlaceableFactory(PlaceableFactory placeableFactory) {
        placeableFactories.remove(placeableFactory);
    }

    public List<PlaceableFactory> getPlaceableFactories() {
        return placeableFactories;
    }

    public void calculateIncrease() {
        HashMap<CurrencyTypes, Float> additionAmount = new HashMap<>();
        for (PlaceableFactory placeableFactory : placeableFactories) {
            for (Map.Entry<CurrencyTypes, Float> e : placeableFactory.onGenerate().entrySet()) {
                float current = e.getValue()*economyBoosts.get(e.getKey());
                if (additionAmount.containsKey(e.getKey())) {
                    current += additionAmount.get(e.getKey());
                    additionAmount.put(e.getKey(), current);
                } else {
                    additionAmount.put(e.getKey(), current);
                }
            }
        }
        boolean isPuppet = overlord!=null;
        for (Map.Entry<CurrencyTypes, Currencies> current : currenciesMap.entrySet()){
            if (additionAmount.containsKey(current.getKey())){
                float addition = additionAmount.get(current.getKey());
                if (isPuppet){
                    float overlordPayment = addition*0.2f;
                    overlord.addPayment(new Payment(current.getKey(),overlordPayment,Component.text()
                            .append(Component.text("Your puppet: ",NamedTextColor.BLUE))
                            .append(nameComponent)
                            .append(Component.text("Has transferred you ",NamedTextColor.BLUE))
                            .append(Component.text(overlordPayment,NamedTextColor.GOLD, TextDecoration.BOLD))
                            .build()
                    ));
                    current.getValue().add(addition*0.8f);
                    return;
                }
                current.getValue().add(addition);
            }
        }
    }
    public void capitulate(Country attacker){
        broadcast(mergeComp(getPrefixes("country"),compBuild(this.name+" has capitulated to "+attacker.name,NamedTextColor.RED)), capital.getInstance());
        for (Province p : new ArrayList<>(this.occupies)){
            p.setOccupier(attacker);
        }
    }
    public void addPayment(Payment payment){
        currenciesMap.get(payment.getCurrencyType()).add(payment.getAmount());
        if (payment.getMessage()!=null){
            sendActionBar(payment.getMessage());
        }
    }
    public void removePayment(Payment payment){
        currenciesMap.get(payment.getCurrencyType()).minus(payment.getAmount());
    }
    public void subtractPayment(Payment payment){
        currenciesMap.get(payment.getCurrencyType()).minus(payment.getAmount());
    }
    public boolean canMinusCost(Payment cost){
        return currenciesMap.containsKey(cost.getCurrencyType()) && currenciesMap.get(cost.getCurrencyType()).getAmount()>=cost.getAmount();
    }
    public void setType(CountryEnums.Type newType){
        type = newType;
    }
    public CountryEnums.Type getType(){
        return type;
    }
    public void setHistory(CountryEnums.History history){
        this.history = history;
    }
    public CountryEnums.History getHistory(){
        return history;
    }

    public Ideology getIdeology() {
        return ideology;
    }
    @Override
    protected Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Country> getWars(){
        return wars;
    }
    public void addWar(Country country){
        wars.add(country);
    }
    public void removeWar(Country country){
        wars.remove(country);
    }
    public void setFocuses(CountryEnums.Focuses f){
        this.focuses = f;
    }
    public CountryEnums.Focuses getFocuses(){
        return focuses;
    }
    public void setPreviousWar(CountryEnums.PreviousWar p){
        this.previousWar = p;
    }
    public CountryEnums.PreviousWar getPreviousWar(){
        return previousWar;
    }
    public void setRelationsStyle(CountryEnums.RelationsStyle relationsStyle){
        this.relationsStyle = relationsStyle;
    }
    public CountryEnums.RelationsStyle getRelationsStyle() {
        return relationsStyle;
    }
    public void setPrefix(Component prefix){
        this.prefix = prefix;
    }
    public Component getPrefix(){
        return prefix;
    }
    public void sendMessage(Component msg){
        for (Player p : players){
            p.sendMessage(msg);
        }
    }
    public void sendActionBar(Component msg){
        for (Player p : players){
            p.sendActionBar(msg);
        }
    }
    public void setLeader(Leader leader){
        this.leader = leader;
        leader.getModifier().forEach((this::addModifier));

        createInfo();
    }
    public Leader getLeader(){
        return leader;
    }
    public void addBoost(CurrencyBoost currencyBoost){
        if (economyBoosts.containsKey(currencyBoost.currencyTypes())){
            economyBoosts.put(currencyBoost.currencyTypes(),economyBoosts.get(currencyBoost.currencyTypes())+currencyBoost.boost());
        }else {
            this.economyBoosts.put(currencyBoost.currencyTypes(),currencyBoost.boost()+1f);
        }
    }
    public void removeBoost(CurrencyBoost currencyBoost){
        if (economyBoosts.containsKey(currencyBoost.currencyTypes())){
            economyBoosts.put(currencyBoost.currencyTypes(),economyBoosts.get(currencyBoost.currencyTypes())-currencyBoost.boost());
        }
    }
    public void createInfo(){
        List<Component> modifierComps = new ArrayList<>();
        for (Modifier modifier : modifiers)
            modifierComps.add(modifier.getName()); modifierComps.add(Component.text(", "));
        this.description = Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text(getName(), NamedTextColor.GOLD))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(Component.text()
                        .append(getLeader().getName())
                        .clickEvent(ClickEvent.runCommand("/country leader "+getName()))
                        .hoverEvent(HoverEvent.showText(getLeader().getDescription()))
                )
                .appendNewline()
                .append(Component.text("Modifiers: "))
                .append(modifierComps)
                .appendNewline()
                .appendNewline()
                .append(Component.text()
                        .append(Component.text("[JOIN]", NamedTextColor.GOLD))
                        .clickEvent(ClickEvent.runCommand("/country join " + getName()))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to join a country", NamedTextColor.GOLD)))
                )
                .build();
    }
    public Component getDescription(){
        return description;
    }

    public Election getElections() {
        return elections;
    }

    public void addRegion(Region region){
        this.region.add(region);
    }

    public List<Region> getRegion(){
        return region;
    }
    public float getMaxBoost(){
        return maxBoost;
    }
    public List<Country> getPuppets(){
        return puppets;
    }
    public void addPuppet(Country country){
        puppets.add(country);
    }
    public void setOverlord(Country country){
        this.overlord = country;
    }
    public Country getOverlord(){
        return overlord;
    }

    public HashMap<CurrencyTypes, Float> getEconomyBoosts() {
        return economyBoosts;
    }
}
