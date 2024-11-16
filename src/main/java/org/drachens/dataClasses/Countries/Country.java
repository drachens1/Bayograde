package org.drachens.dataClasses.Countries;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Scheduler;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Diplomacy.demand.Demand;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.*;
import org.drachens.dataClasses.Modifier;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.TextDisplay;
import org.drachens.dataClasses.territories.Region;
import org.drachens.events.Countries.CountryChangeEvent;
import org.drachens.events.Countries.CountryJoinEvent;
import org.drachens.events.Countries.CountryLeaveEvent;
import org.drachens.events.EndWarEvent;
import org.drachens.events.Factions.FactionJoinEvent;
import org.drachens.interfaces.MapGen;
import org.drachens.temporary.Factory;
import org.drachens.util.AStarPathfinder;

import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.drachens.util.KyoriUtil.*;
import static org.drachens.util.Messages.broadcast;
import static org.drachens.util.Messages.globalBroadcast;

public class Country implements Cloneable {
    private Player playerLeader;
    private final MapGen mapGen;
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    private Leader leader;
    private final List<Player> players;
    private HashMap<CurrencyTypes, Currencies> currenciesMap;
    private final List<Material> city = new ArrayList<>();
    private final HashMap<BuildTypes, List<Building>> buildTypesListHashMap = new HashMap<>();
    private EconomyFactionType economyFactionType;
    private MilitaryFactionType militaryFactionType;
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
    private float maxBuildingSlotBoost = 1f;
    private float capitulationBoostPercentage = 1f;
    private final List<Region> region = new ArrayList<>();
    private final List<Modifier> modifiers = new ArrayList<>();
    private final HashMap<CurrencyTypes, Float> economyBoosts = new HashMap<>();
    private Country overlord = null;
    private final List<Country> puppets = new ArrayList<>();
    private final HashMap<Province, Material> majorCityBlocks = new HashMap<>();
    private float relationsBoost = 0f;
    private float totalProductionBoost = 1f;
    private final AStarPathfinder aStarPathfinder;
    private final Instance instance;
    private final List<Clientside> clientsides = new ArrayList<>();
    private final List<Clientside> allyTroopClientsides = new ArrayList<>();
    private final List<Troop> troops = new ArrayList<>();
    private final List<Player> playerInvites = new ArrayList<>();
    private final List<String> factionInvites = new ArrayList<>();
    private final HashMap<Country, Demand> demandHashMap = new HashMap<>();
    private final List<String> demandCountryNames = new ArrayList<>();

    public Country(HashMap<CurrencyTypes, Currencies> startingCurrencies, String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Election election, Instance instance) {
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
        this.instance = instance;
        aStarPathfinder = new AStarPathfinder(this, ContinentalManagers.world(instance).provinceManager());
        this.mapGen = ContinentalManagers.world(instance).votingManager().getWinner().getMapGenerator();
    }

    public void addModifier(Modifier modifier) {
        addModifier(modifier, false);
    }

    public void addModifier(Modifier modifier, boolean update) {
        if (modifiers.contains(modifier)) {

            return;
        }

        modifiers.add(modifier);
        modifier.addCountry(this);
        this.maxBuildingSlotBoost += modifier.getMaxBuildingSlotBoost();
        this.capitulationBoostPercentage += modifier.getCapitulationBoostPercentage();
        this.totalProductionBoost += modifier.getProductionBoost();
        modifier.getCurrencyBoostList().forEach(this::addBoost);
        if (!update) createInfo();
    }

    public void updateModifier(Modifier modifier, Modifier old) {
        removeModifier(old);
        addModifier(modifier, true);
    }

    public void removeModifier(Modifier modifier) {
        if (!modifiers.contains(modifier))
            return;

        modifiers.remove(modifier);
        modifier.removeCountry(this);
        this.maxBuildingSlotBoost -= modifier.getMaxBuildingSlotBoost();
        this.capitulationBoostPercentage -= modifier.getCapitulationBoostPercentage();
        this.totalProductionBoost -= modifier.getProductionBoost();
        modifier.getCurrencyBoostList().forEach(this::removeBoost);
        createInfo();
    }

    public List<Modifier> getModifiers() {
        return modifiers;
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
        this.maxCapitulationPoints += this.city.indexOf(city.getMaterial());
        this.capitulationPoints += this.city.indexOf(city.getMaterial());
        this.cities.add(city);
    }

    public void removeCity(Province city) {
        this.capitulationPoints += this.city.indexOf(city.getMaterial());
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

    public void addPlayer(CPlayer p) {
        EventDispatcher.call(new CountryJoinEvent(this, p));
        this.players.add(p);
        p.sendMessage(mergeComp(getPrefixes("country"), replaceString(getCountryMessages("countryJoin"), "%country%", this.name)));
        broadcast(mergeComp(getPrefixes("country"), replaceString(replaceString(getCountryMessages("broadcastedCountryJoin"), "%country%", this.name), "%player%", p.getUsername())), p.getInstance());
        p.teleport(capital.getPos().add(0, 1, 0));
        clientsides.forEach(clientside -> clientside.addViewer(p));
        if (playerLeader == null)
            setPlayerLeader(p);
    }

    private final DemandManager demandManager = ContinentalManagers.demandManager;
    public void removePlayer(Player p, boolean left) {
        if (left) EventDispatcher.call(new CountryLeaveEvent(this, p));
        this.players.remove(p);
        p.sendMessage(mergeComp(getPrefixes("country"), replaceString(getCountryMessages("countryLeave"), "%country%", this.name)));
        clientsides.forEach(clientside -> clientside.removeViewer(p));
        if (isPlayerLeader(p)) {
            if (players.isEmpty()) {
                setPlayerLeader(null);
            } else
                setPlayerLeader(players.getFirst());
        }
        demandManager.removeActive(p);
    }

    public void changeCountry(CPlayer p) {
        Country country = p.getCountry();

        if (country != null) {
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

    public void cityCaptured(Country attacker, Province capturedCity) {
        removeCity(capturedCity);
        if (!capitulated) {
            if (capital == capturedCity) {
                broadcast(mergeComp(getPrefixes("country"), compBuild(attacker.name + " has seized the " + name + " capital", NamedTextColor.RED)), capital.getInstance());
            }
        }
        System.out.println(capitulationPoints + " : " + maxCapitulationPoints * (0.8 * capitulationBoostPercentage) + " : " + 0.8 * capitulationBoostPercentage);
        if (capitulationPoints >= maxCapitulationPoints * (0.8 * capitulationBoostPercentage) && !capitulated) {
            capitulated = true;
            capitulate(attacker);
        }
    }

    public void calculateIncrease() {
        HashMap<CurrencyTypes, Float> additionAmount = new HashMap<>();
        List<Building> buildings = buildTypesListHashMap.get(ContinentalManagers.defaultsStorer.buildingTypes.getBuildType("factory"));
        if (buildings != null)
            buildings.forEach((building -> {
                if (building.getBuildTypes() instanceof Factory factory) {
                    Payments payments1 = factory.generate(building);
                    List<Component> comps = new ArrayList<>();
                    payments1.getPayments().forEach((payment -> {
                        float addition = payment.getAmount();
                        if (economyBoosts.get(payment.getCurrencyType()) != null) {
                            addition = addition * totalProductionBoost;
                            addition = addition * economyBoosts.get(payment.getCurrencyType());
                        }
                        if (additionAmount.containsKey(payment.getCurrencyType())) {
                            additionAmount.put(payment.getCurrencyType(), additionAmount.get(payment.getCurrencyType()) + addition);
                        } else {
                            additionAmount.put(payment.getCurrencyType(), addition);
                        }
                        comps.add(Component.text(addition));
                        comps.add(payment.getCurrencyType().getSymbol());
                        comps.add(Component.newline());
                    }));
                    createFloatingText(building, Component.text().append(comps).build());
                }
            }));
        boolean isPuppet = overlord != null;
        if (isPuppet) {
            for (Map.Entry<CurrencyTypes, Float> addition : additionAmount.entrySet()) {
                float add = addition.getValue() * 0.8f;
                float overlordPayment = addition.getValue() * 0.2f;
                if (currenciesMap.containsKey(addition.getKey())) {
                    currenciesMap.get(addition.getKey()).add(add);
                } else {
                    currenciesMap.put(addition.getKey(), new Currencies(addition.getKey(), add));
                }
                overlord.addPayment(new Payment(addition.getKey(), overlordPayment, Component.text()
                        .append(Component.text("Your puppet: ", NamedTextColor.BLUE))
                        .append(nameComponent)
                        .append(Component.text("Has transferred you ", NamedTextColor.BLUE))
                        .append(Component.text(overlordPayment, NamedTextColor.GOLD, TextDecoration.BOLD))
                        .build()));
            }
        } else {
            for (Map.Entry<CurrencyTypes, Float> addition : additionAmount.entrySet()) {
                if (currenciesMap.containsKey(addition.getKey())) {
                    currenciesMap.get(addition.getKey()).add(addition.getValue());
                } else {
                    currenciesMap.put(addition.getKey(), new Currencies(addition.getKey(), addition.getValue()));
                }
            }
        }
    }

    public void capitulate(Country attacker) {
        broadcast(mergeComp(getPrefixes("country"), compBuild(this.name + " has capitulated to " + attacker.name, NamedTextColor.RED)), capital.getInstance());
        for (Province p : new ArrayList<>(this.occupies)) {
            p.setOccupier(attacker);
        }
        wars.forEach(aggressor -> EventDispatcher.call(new EndWarEvent(aggressor, this)));
    }

    public void addPayment(Payment payment) {
        currenciesMap.get(payment.getCurrencyType()).add(payment.getAmount());
        if (payment.getMessage() != null) {
            sendActionBar(payment.getMessage());
        }
    }

    public void addPayments(Payments payments) {
        currenciesMap = payments.addPayments(currenciesMap);
    }

    public void removePayments(Payments payments) {
        currenciesMap = payments.minusPayments(currenciesMap);
    }

    public void removePayment(Payment payment) {
        currenciesMap.get(payment.getCurrencyType()).minus(payment.getAmount());
    }

    public float subtractMaximumAmountPossible(Payment payment) {
        float currentBalance = currenciesMap.get(payment.getCurrencyType()).getAmount();
        float withRemoved = currentBalance - payment.getAmount();
        if (withRemoved < 0) return Math.abs(withRemoved);
        return 0f;
    }

    public boolean canMinusCost(Payment cost) {
        return currenciesMap.containsKey(cost.getCurrencyType()) && currenciesMap.get(cost.getCurrencyType()).getAmount() >= cost.getAmount();
    }

    public boolean canMinusCosts(Payments cost) {
        for (Payment payment : cost.getPayments())
            if (currenciesMap.containsKey(payment.getCurrencyType()) && currenciesMap.get(payment.getCurrencyType()).getAmount() < payment.getAmount())
                return false;

        return true;
    }

    public void addTextDisplay(TextDisplay textDisplay) {
        players.forEach(textDisplay::addViewer);
        this.clientsides.add(textDisplay);
    }

    public void removeTextDisplay(TextDisplay textDisplay) {
        players.forEach(textDisplay::removeViewer);
        this.clientsides.remove(textDisplay);
    }

    public void setType(CountryEnums.Type newType) {
        type = newType;
    }

    public CountryEnums.Type getType() {
        return type;
    }

    public void setHistory(CountryEnums.History history) {
        this.history = history;
    }

    public CountryEnums.History getHistory() {
        return history;
    }

    public Ideology getIdeology() {
        return ideology;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Country> getWars() {
        return wars;
    }

    public void addWar(Country country) {
        wars.add(country);
    }

    public void removeWar(Country country) {
        wars.remove(country);
    }

    public Boolean atWar(Country country) {
        return wars.contains(country);
    }

    public void setFocuses(CountryEnums.Focuses f) {
        this.focuses = f;
    }

    public CountryEnums.Focuses getFocuses() {
        return focuses;
    }

    public void setPreviousWar(CountryEnums.PreviousWar p) {
        this.previousWar = p;
    }

    public CountryEnums.PreviousWar getPreviousWar() {
        return previousWar;
    }

    public void setRelationsStyle(CountryEnums.RelationsStyle relationsStyle) {
        this.relationsStyle = relationsStyle;
    }

    public CountryEnums.RelationsStyle getRelationsStyle() {
        return relationsStyle;
    }

    public void setPrefix(Component prefix) {
        this.prefix = prefix;
    }

    public Component getPrefix() {
        return prefix;
    }

    public void sendMessage(Component msg) {
        for (Player p : players) {
            p.sendMessage(msg);
        }
    }

    public void sendActionBar(Component msg) {
        for (Player p : players) {
            p.sendActionBar(msg);
        }
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
        leader.getModifier().forEach((this::addModifier));

        createInfo();
    }

    public Leader getLeader() {
        return leader;
    }

    public void addBoost(CurrencyBoost currencyBoost) {
        if (economyBoosts.containsKey(currencyBoost.currencyTypes())) {
            economyBoosts.put(currencyBoost.currencyTypes(), economyBoosts.get(currencyBoost.currencyTypes()) + currencyBoost.boost());
        } else {
            this.economyBoosts.put(currencyBoost.currencyTypes(), currencyBoost.boost() + 1f);
        }
    }

    public void removeBoost(CurrencyBoost currencyBoost) {
        if (economyBoosts.containsKey(currencyBoost.currencyTypes())) {
            economyBoosts.put(currencyBoost.currencyTypes(), economyBoosts.get(currencyBoost.currencyTypes()) - currencyBoost.boost());
        }
    }

    public void createInfo() {
        if (mapGen.isGenerating(instance)) return;
        globalBroadcast("updated info");
        List<Component> modifierComps = new ArrayList<>();
        for (Modifier modifier : modifiers) {
            modifierComps.add(modifier.getName());
            modifierComps.add(Component.text(", "));
        }
        Component leaderComp = Component.text()
                .append(Component.text("Faction: "))
                .build();

        List<Component> factionsComps = new ArrayList<>();
        EconomyFactionType economyFactionType1 = getEconomyFactionType();
        if (isInAnEconomicFaction()) {
            factionsComps.add(Component.text()
                    .append(economyFactionType1.getName())
                    .append(compBuild(" : ", NamedTextColor.WHITE))
                    .append(economyFactionType1.getNameComponent())
                    .build());
        }
        MilitaryFactionType militaryFactionType1 = getMilitaryFactionType();
        if (isInAMilitaryFaction()) {
            factionsComps.add(Component.text()
                    .append(militaryFactionType1.getName())
                    .append(compBuild(" : ", NamedTextColor.WHITE))
                    .append(militaryFactionType1.getNameComponent())
                    .build());
        }

        this.description = Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text(getName(), NamedTextColor.GOLD))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(Component.text()
                        .append(getLeader().getName())
                        .clickEvent(ClickEvent.runCommand("/country leader " + getName()))
                        .hoverEvent(HoverEvent.showText(getLeader().getDescription()))
                )
                .appendNewline()
                .append(Component.text("Modifiers: "))
                .append(modifierComps)
                .appendNewline()
                .append(leaderComp)
                .append(factionsComps)
                .appendNewline()
                .append(Component.text()
                        .append(Component.text("[JOIN]", NamedTextColor.GOLD))
                        .clickEvent(ClickEvent.runCommand("/country join " + getName()))
                        .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to join a country", NamedTextColor.GOLD)))
                )
                .build();

        this.nameComponent = Component.text()
                .append(Component.text(name, NamedTextColor.GOLD, TextDecoration.BOLD))
                .clickEvent(ClickEvent.runCommand("/country info " + getName()))
                .hoverEvent(description)
                .build();
    }

    public Component getDescription() {
        return description;
    }

    public Election getElections() {
        return elections;
    }

    public void addRegion(Region region) {
        this.region.add(region);
    }

    public List<Region> getRegion() {
        return region;
    }

    public float getMaxBuildingSlotBoost() {
        return maxBuildingSlotBoost;
    }

    public List<Country> getPuppets() {
        return puppets;
    }

    public void addPuppet(Country country) {
        puppets.add(country);
    }

    public void setOverlord(Country country) {
        this.overlord = country;
    }

    public Country getOverlord() {
        return overlord;
    }

    public HashMap<CurrencyTypes, Float> getEconomyBoosts() {
        return economyBoosts;
    }

    public void endGame() {
        //aiCompetitor.kill();
    }

    public void addMajorCity(Province province, Material material) {
        majorCityBlocks.put(province, material);
    }

    public Material getMajorCity(Province province) {
        return majorCityBlocks.get(province);
    }

    public boolean isMajorCity(Province province) {
        return majorCityBlocks.containsKey(province);
    }

    public void setEconomyFactionType(EconomyFactionType economyFactionType) {
        this.economyFactionType = economyFactionType;
    }

    public void setMilitaryFactionType(MilitaryFactionType militaryFactionType) {
        this.militaryFactionType = militaryFactionType;
    }

    public boolean canJoinAFaction() {
        return getEconomyFactionType() == null || getMilitaryFactionType() == null;
    }

    public boolean canJoinFaction(Factions factions) {
        return (factions instanceof MilitaryFactionType && getMilitaryFactionType() == null) || (factions instanceof EconomyFactionType && getEconomyFactionType() == null);
    }

    public void joinMilitaryFaction(MilitaryFactionType militaryFactionType) {
        if (!canJoinFaction(militaryFactionType)) return;
        setMilitaryFactionType(militaryFactionType);
        EventDispatcher.call(new FactionJoinEvent(militaryFactionType, this));
        createInfo();
    }

    public void joinEconomyFaction(EconomyFactionType economyFactionType) {
        if (!canJoinFaction(economyFactionType)) return;
        setEconomyFactionType(economyFactionType);
        economyFactionType.addCountry(this);
        EventDispatcher.call(new FactionJoinEvent(economyFactionType, this));
        createInfo();
    }

    public void joinFaction(Factions factions) {
        if (factions instanceof EconomyFactionType) {
            joinEconomyFaction((EconomyFactionType) factions);
        } else
            joinMilitaryFaction((MilitaryFactionType) factions);
    }

    public EconomyFactionType getEconomyFactionType() {
        return economyFactionType;
    }

    public MilitaryFactionType getMilitaryFactionType() {
        return militaryFactionType;
    }

    public boolean isLeaderOfAFaction() {
        return isEconomyFactionLeader() || isMilitaryFactionLeader();
    }

    public boolean isEconomyFactionLeader() {
        return economyFactionType != null && economyFactionType.getLeader() == this;
    }

    public boolean isMilitaryFactionLeader() {
        return militaryFactionType != null && militaryFactionType.getLeader() == this;
    }

    public AStarPathfinder getaStarPathfinder() {
        return aStarPathfinder;
    }

    public Instance getInstance() {
        return instance;
    }

    public boolean isMilitaryAlly(Country country) {
        return militaryFactionType != null && militaryFactionType.getMembers().contains(country);
    }

    public boolean isEconomicAlly(Country country) {
        return economyFactionType != null && economyFactionType.getMembers().contains(country);
    }

    public boolean isAlly(Country country){
        return isMilitaryAlly(country)||isEconomicAlly(country);
    }

    public boolean isInAFaction() {
        return isInAMilitaryFaction() || isInAnEconomicFaction();
    }

    public boolean isInAMilitaryFaction() {
        return militaryFactionType != null;
    }

    public boolean isInAnEconomicFaction() {
        return economyFactionType != null;
    }

    public boolean isInAllFactions() {
        return !isInAMilitaryFaction() || !isInAnEconomicFaction();
    }

    public void addBuilding(Building building) {
        clientsides.add(building.getItemDisplay());
        building.getItemDisplay().addCountry(this);
        if (buildTypesListHashMap.containsKey(building.getBuildTypes())) {
            List<Building> buildings = buildTypesListHashMap.get(building.getBuildTypes());
            buildings.add(building);
            buildTypesListHashMap.put(building.getBuildTypes(), buildings);
            return;
        }
        List<Building> buildings = new ArrayList<>();
        buildings.add(building);
        buildTypesListHashMap.put(building.getBuildTypes(), buildings);
    }

    public void removeBuilding(Building building) {
        players.forEach(player -> building.getItemDisplay().removeViewer(player));
        if (buildTypesListHashMap.containsKey(building.getBuildTypes())) {
            List<Building> buildings = buildTypesListHashMap.get(building.getBuildTypes());
            buildings.remove(building);
            buildTypesListHashMap.put(building.getBuildTypes(), buildings);
        }
        clientsides.remove(building.getItemDisplay());
    }

    public List<Building> getBuildings(BuildTypes buildTypes) {
        return buildTypesListHashMap.get(buildTypes);
    }

    public void addTroop(Troop troop) {
        troop.getTroop().addCountry(this);
        troops.add(troop);
        clientsides.add(troop.getTroop());
        clientsides.add(troop.getAlly());
    }

    public void removeTroop(Troop troop) {
        troop.getTroop().removeCountry(this);
        troops.remove(troop);
        clientsides.remove(troop.getTroop());
    }

    private void createFloatingText(Building building, Component text) {
        Province province = building.getProvince();
        long initialDelay = new Random().nextLong(0, 200);
        scheduler.buildTask(() -> {
            TextDisplay textDisplay = new TextDisplay.create(province, text)
                    .setFollowPlayer(true)
                    .setLineWidth(40)
                    .withOffset()
                    .build();
            addTextDisplay(textDisplay);
            scheduler.buildTask(() -> {
                textDisplay.moveNoRotation(new Pos(0, 4, 0), 40, true);
                textDisplay.destroy(3995L);
            }).delay(initialDelay + new Random().nextLong(0, 200), ChronoUnit.MILLIS).schedule();
        }).delay(initialDelay, ChronoUnit.MILLIS).schedule();
    }

    public void loadClientsides(List<Clientside> clientsides) {
        clientsides.forEach(clientside -> players.forEach(clientside::addViewer));
        this.clientsides.addAll(clientsides);
    }

    public void unloadClientsides(List<Clientside> clientsides) {
        clientsides.forEach(clientside -> players.forEach(clientside::removeViewer));
        this.clientsides.removeAll(clientsides);
    }

    public List<Clientside> getAlliedTroopClientsides() {
        return allyTroopClientsides;
    }

    public void loadClientside(Clientside clientside) {
        players.forEach(clientside::addViewer);
        this.clientsides.add(clientside);
    }

    public void unloadClientside(Clientside clientside) {
        players.forEach(clientside::removeViewer);
        this.clientsides.remove(clientside);
    }

    public void inviteToFaction(Factions factions) {
        factionInvites.add(factions.getStringName());
    }

    public void removeInviteToFaction(Factions factions) {
        factionInvites.remove(factions.getStringName());
    }

    public List<String> getInvitedToFactions() {
        return factionInvites;
    }

    public void setPlayerLeader(Player player) {
        this.playerLeader = player;
    }

    public boolean isPlayerLeader(Player player) {
        return playerLeader == player;
    }

    public void invitePlayer(Player player) {
        playerInvites.add(player);
    }

    public void removeInvite(Player player) {
        playerInvites.remove(player);
    }

    public boolean hasInvited(Player player) {
        return playerInvites.contains(player);
    }

    public void sendDemand(Demand demand){
        demandHashMap.put(demand.getFromCountry(),demand);
        demandCountryNames.add(demand.getFromCountry().name);
    }

    public boolean hasAnyDemands(){
        return !demandHashMap.isEmpty();
    }

    public boolean hasDemand(Country country){
        return demandHashMap.containsKey(country);
    }

    public List<String> getDemandCountryNames(){
        return demandCountryNames;
    }
}
