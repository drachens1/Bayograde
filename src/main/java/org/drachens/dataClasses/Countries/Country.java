package org.drachens.dataClasses.Countries;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.bossbars.CapitulationBar;
import org.drachens.dataClasses.BoostEnum;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.dataClasses.Diplomacy.Relations;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.Loan;
import org.drachens.dataClasses.Economics.Stability;
import org.drachens.dataClasses.Economics.Vault;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.ImaginaryWorld;
import org.drachens.dataClasses.Modifier;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.CompletionBarTextDisplay;
import org.drachens.dataClasses.other.TextDisplay;
import org.drachens.dataClasses.territories.Province;
import org.drachens.events.NewDay;
import org.drachens.events.countries.CountryChangeEvent;
import org.drachens.events.countries.CountryJoinEvent;
import org.drachens.events.countries.CountryLeaveEvent;
import org.drachens.events.countries.war.CapitulationEvent;
import org.drachens.events.countries.war.EndWarEvent;
import org.drachens.events.countries.warjustification.WarJustificationCompletionEvent;
import org.drachens.events.countries.warjustification.WarJustificationExpiresEvent;
import org.drachens.interfaces.MapGen;
import org.drachens.temporary.scoreboards.country.DefaultCountryScoreboard;
import org.drachens.util.AStarPathfinderXZ;
import org.drachens.util.MessageEnum;

import java.util.*;

import static org.drachens.util.Messages.broadcast;

public abstract class Country implements Cloneable {
    private CompletionBarTextDisplay capitulationTextBar;
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    private final MapGen mapGen;
    private final List<CPlayer> players;
    private final Vault vault;
    private final List<Material> city = new ArrayList<>();
    private final HashMap<BuildingEnum, List<Building>> buildTypesListHashMap = new HashMap<>();
    private final List<Country> wars = new ArrayList<>();
    private final Ideology ideology;
    private final Election elections;
    private final List<Modifier> modifiers = new ArrayList<>();
    private final List<Country> puppets = new ArrayList<>();
    private final HashMap<Province, Material> majorCityBlocks = new HashMap<>();
    private final AStarPathfinderXZ aStarPathfinder;
    private final Instance instance;
    private final List<Clientside> clientsides = new ArrayList<>();
    private final List<Player> playerInvites = new ArrayList<>();
    private final List<String> factionInvites = new ArrayList<>();
    private final HashMap<String, Demand> demandHashMap = new HashMap<>();
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private Player playerLeader;
    private Leader leader;
    private EconomyFactionType economyFactionType;
    private MilitaryFactionType militaryFactionType;
    private final List<Province> occupies;
    private final List<Province> cities;
    private String name;
    private Component nameComponent;
    private Material block;
    private Material border;
    private Province capital;
    private float capitulationPoints;
    private float maxCapitulationPoints;
    private boolean capitulated = false;
    private Component prefix;
    private Component description;
    private Country overlord = null;
    private final Stability stability = new Stability(50f, this);
    private final Relations relations = new Relations(this);
    private final HashMap<String, Loan> loanRequests = new HashMap<>();
    private final CapitulationBar capitulationBar = new CapitulationBar();
    private final HashMap<BoostEnum, Float> boostHashmap = new HashMap<>();
    private final ImaginaryWorld warsWorld;
    private final ImaginaryWorld allyWorld;
    private final HashMap<String, Demand> outgoingDemands = new HashMap<>();
    private final HashMap<String, WarJustification> warJustificationHashMap = new HashMap<>();
    private final HashMap<String, WarJustification> completedWarJustifications = new HashMap<>();
    private final HashMap<String, NonAggressionPact> nonAggressionPactHashMap = new HashMap<>();
    private final HashSet<Province> cores = new HashSet<>();
    private final HashMap<String, List<Province>> occupiesThereCores = new HashMap<>();

    public Country(String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Election election, Instance instance, Vault vault) {
        this.occupies = new ArrayList<>();
        this.vault = vault;
        vault.setCountry(this);
        this.nameComponent = nameComponent;
        this.name = name;
        this.setPrefix(Component.text(name, NamedTextColor.BLUE));
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
        aStarPathfinder = new AStarPathfinderXZ(ContinentalManagers.world(instance).provinceManager());
        this.mapGen = ContinentalManagers.world(instance).dataStorer().votingOption.getMapGenerator();
        warsWorld=new ImaginaryWorld(instance,true);
        allyWorld=new ImaginaryWorld(instance,true);

    }

    public void init(){
        List<Country> countries = new ArrayList<>(ContinentalManagers.world(instance).countryDataManager().getCountries());
        countries.remove(this);
        countries.forEach(country -> country.getOccupies().forEach(province -> {
            warsWorld.addGhostBlock(province.getPos(),Block.GRAY_CONCRETE);
            allyWorld.addGhostBlock(province.getPos(),Block.GRAY_CONCRETE);
        }));
    }

    public ImaginaryWorld getWarsWorld(){
        return warsWorld;
    }

    public ImaginaryWorld getAllyWorld(){
        return allyWorld;
    }

    public void addCore(Province province){
        cores.add(province);
    }

    public void removeCore(Province province){
        cores.remove(province);
    }

    public boolean hasCore(Province province){
        return cores.contains(province);
    }

    public List<Province> getCores(){
        return new ArrayList<>(cores);
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
        modifier.getBoostHashMap().forEach(this::addBoost);
        if (!update && modifier.shouldDisplay()) createInfo();
    }

    public void addBoost(BoostEnum boostEnum, float value) {
        float current = boostHashmap.getOrDefault(boostEnum, 1f);
        boostHashmap.put(boostEnum, value + current);
    }

    public void minusBoost(BoostEnum boostEnum, float value) {
        float current = boostHashmap.getOrDefault(boostEnum, 1f);
        boostHashmap.put(boostEnum, current - value);
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
        modifier.getBoostHashMap().forEach(this::minusBoost);
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

    public void addCity(Province city) {
        this.maxCapitulationPoints += this.city.indexOf(city.getMaterial());
        this.cities.add(city);
    }

    public void removeCity(Province city) {
        this.capitulationPoints += this.city.indexOf(city.getMaterial());
        this.cities.remove(city);
    }

    public void dontRemovePoints(Province city){
        this.maxCapitulationPoints-=this.city.indexOf(city.getMaterial());
        this.cities.remove(city);
    }

    public List<Province> getOccupies() {
        return occupies;
    }

    public void addOccupied(Province province) {
        province.getCorers().forEach(country -> addOthersCores(country,province));
        occupies.add(province);
    }

    public void captureProvince(Province province){
        warsWorld.removeGhostBlock(province.getPos());
        allyWorld.removeGhostBlock(province.getPos());
        addOccupied(province);
    }

    public void removeOccupied(Province province) {
        province.getCorers().forEach(country -> removeOthersCores(country,province));
        occupies.remove(province);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(Component name) {
        this.nameComponent = name;
    }

    public Component getNameComponent() {
        return nameComponent;
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
        if (capitulationTextBar!=null)capitulationTextBar.getTextDisplay().dispose();
        this.capitulationTextBar=new CompletionBarTextDisplay(capital.getPos().add(0,3,0),capital.getInstance(), TextColor.color(0,255,0));
    }

    public void addPlayer(CPlayer p) {
        EventDispatcher.call(new CountryJoinEvent(this, p));
        capitulationBar.addPlayer(p);
        this.players.add(p);
        p.sendMessage(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text("You have joined ", NamedTextColor.BLUE).append(nameComponent)).build()).build());
        broadcast(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text(p.getUsername(), NamedTextColor.GOLD, TextDecoration.BOLD)).append(Component.text(" has joined ", NamedTextColor.BLUE)).append(nameComponent).build()).build(), p.getInstance());
        p.teleport(capital.getPos().add(0, 1, 0));
        scoreboardManager.openScoreboard(new DefaultCountryScoreboard(), p);
        DefaultCountryScoreboard defaultCountryScoreboard = (DefaultCountryScoreboard) scoreboardManager.getScoreboard(p);
        defaultCountryScoreboard.openEconomy();
        clientsides.forEach(clientside -> clientside.addViewer(p));
        if (playerLeader == null)
            setPlayerLeader(p);
        onAddPlayer(p);
    }

    protected abstract void onAddPlayer(CPlayer p);

    public void removePlayer(CPlayer p, boolean left) {
        if (left) EventDispatcher.call(new CountryLeaveEvent(this, p));
        capitulationBar.removePlayer(p);
        this.players.remove(p);
        p.sendMessage(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text("You have left ", NamedTextColor.BLUE)).append(nameComponent).build()).build());
        clientsides.forEach(clientside -> clientside.removeViewer(p));
        if (isPlayerLeader(p)) {
            if (players.isEmpty()) {
                setPlayerLeader(null);
            } else
                setPlayerLeader(players.getFirst());
        }
        demandManager.removeActive(p.getCountry());
        onRemovePlayer(p);
        warsWorld.removePlayer(p);
        allyWorld.removePlayer(p);
    }

    protected abstract void onRemovePlayer(CPlayer p);


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

    public List<CPlayer> getPlayer() {
        return players;
    }

    public void cityCaptured(Country attacker, Province capturedCity) {
        removeCity(capturedCity);
        if (!capitulated) {
            if (capital == capturedCity) {
                broadcast(Component.text().append(MessageEnum.country.getComponent(), Component.text(attacker.name + " has seized the " + name + " capital", NamedTextColor.RED)).build(), capital.getInstance());
            }
        }
        float capPercentage = bound(0.5f * boostHashmap.getOrDefault(BoostEnum.capitulation, 1f));
        if (capPercentage>=1f)capPercentage=0.85f;
        float capPoints = maxCapitulationPoints * capPercentage;
        float progress = capitulationPoints / capPoints;
        capitulationTextBar.setProgress(1f-progress);
        capitulationBar.setProgress(progress);
        if (capitulationPoints >= capPoints && !capitulated) {
            EventDispatcher.call(new CapitulationEvent(this,attacker));
        }
    }

    private float bound(float d) {
        if (d > 1) return 1;
        if (d < 0) return 0;
        return d;
    }

    public void capitulate(Country attacker) {
        capitulated = true;
        for (Province p : new ArrayList<>(this.occupies)) {
            p.capture(attacker);
        }
        new ArrayList<>(wars).forEach(aggressor -> EventDispatcher.call(new EndWarEvent(aggressor, this)));
    }

    public void addPayment(Payment payment, Component msg) {
        sendMessage(msg);
        vault.addPayment(payment);
    }

    public void addPayments(Payments payments) {
        vault.addPayments(payments);
    }

    public void removePayments(Payments payments) {
        vault.minusPayments(payments);
    }

    public void removePayment(Payment payment) {
        vault.minusPayment(payment);
    }

    public float subtractMaximumAmountPossible(Payment payment) {
        return vault.subtractMaxAmountPossible(payment);
    }

    public boolean canMinusCost(Payment cost) {
        return vault.canMinus(cost);
    }

    public boolean canMinusCosts(Payments cost) {
        return vault.canMinus(cost);
    }

    public void minusThenLoan(Payment payment, Country from) {
        getVault().minusThenLoan(payment, from);
    }


    public void addTextDisplay(TextDisplay textDisplay) {
        players.forEach(textDisplay::addViewer);
        this.clientsides.add(textDisplay);
    }

    public void removeTextDisplay(TextDisplay textDisplay) {
        players.forEach(textDisplay::removeViewer);
        this.clientsides.remove(textDisplay);
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

    public List<String> getWarsString(){
        List<String> s = new ArrayList<>();
        wars.forEach(country -> s.add(country.name));
        return s;
    }

    public void addWar(Country country) {
        wars.add(country);
        country.getOccupies().forEach(province -> warsWorld.removeGhostBlock(province.getPos()));
        addTextDisplay(country.getCapitulationTextBar().getTextDisplay());
    }

    public void removeWar(Country country) {
        wars.remove(country);
        country.getOccupies().forEach(province -> warsWorld.addGhostBlock(province.getPos(),Block.GRAY_CONCRETE));
        removeTextDisplay(country.getCapitulationTextBar().getTextDisplay());
    }

    public Boolean atWar(Country country) {
        return wars.contains(country);
    }

    public Component getPrefix() {
        return prefix;
    }

    public void setPrefix(Component prefix) {
        this.prefix = prefix;
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

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
        leader.getModifier().forEach((this::addModifier));
    }

    public float getBoost(BoostEnum boostEnum) {
        return boostHashmap.getOrDefault(boostEnum, 1f);
    }

    public void createInfo() {
        if (mapGen.isGenerating(instance)) return;
        List<Component> modifierComps = new ArrayList<>();
        for (Modifier modifier : modifiers) {
            if (modifier.getName() == null) continue;
            if (!modifier.shouldDisplay()) continue;
            modifierComps.add(modifier.getName());
            if (modifiers.getLast() != modifier) {
                modifierComps.add(Component.text(" ,"));
            }
        }

        Component leaderComp = Component.text()
                .append(Component.text("Faction: "))
                .build();

        List<Component> factionsComps = new ArrayList<>();
        EconomyFactionType economyFactionType1 = getEconomyFactionType();
        if (isInAnEconomicFaction()) {
            factionsComps.add(Component.text()
                    .append(economyFactionType1.getName())
                    .append(Component.text(" : ", NamedTextColor.WHITE))
                    .append(economyFactionType1.getNameComponent())
                    .build());
        }
        MilitaryFactionType militaryFactionType1 = getMilitaryFactionType();
        if (isInAMilitaryFaction()) {
            factionsComps.add(Component.text()
                    .append(militaryFactionType1.getName())
                    .append(Component.text(" : ", NamedTextColor.WHITE))
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
                .clickEvent(ClickEvent.runCommand("/country info general " + getName()))
                .hoverEvent(description)
                .build();
    }

    public Component getDescription() {
        return description;
    }

    public Election getElections() {
        return elections;
    }

    public List<Country> getPuppets() {
        return puppets;
    }

    public void addPuppet(Country country) {
        puppets.add(country);
    }

    public Country getOverlord() {
        return overlord;
    }

    public void setOverlord(Country country) {
        this.overlord = country;
    }

    public void endGame() {
        //aiCompetitor.kill();
        clientsides.forEach(Clientside::dispose);
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

    public boolean canJoinFaction(Factions factions) {
        return (factions instanceof MilitaryFactionType && getMilitaryFactionType() == null) || (factions instanceof EconomyFactionType && getEconomyFactionType() == null);
    }

    public EconomyFactionType getEconomyFactionType() {
        return economyFactionType;
    }

    public void setEconomyFactionType(EconomyFactionType economyFactionType) {
        this.economyFactionType = economyFactionType;
    }

    public MilitaryFactionType getMilitaryFactionType() {
        return militaryFactionType;
    }

    public void setMilitaryFactionType(MilitaryFactionType militaryFactionType) {
        this.militaryFactionType = militaryFactionType;
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

    public AStarPathfinderXZ getaStarPathfinder() {
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

    public boolean isAlly(Country country) {
        return isMilitaryAlly(country) || isEconomicAlly(country);
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

    public List<Building> getBuildings(BuildingEnum buildingEnum) {
        return buildTypesListHashMap.get(buildingEnum);
    }


    public void loadClientsides(List<Clientside> clientsides) {
        clientsides.forEach(clientside -> players.forEach(clientside::addViewer));
        this.clientsides.addAll(clientsides);
    }

    public void unloadClientsides(List<Clientside> clientsides) {
        clientsides.forEach(clientside -> players.forEach(clientside::removeViewer));
        this.clientsides.removeAll(clientsides);
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

    public void sendDemand(Demand demand) {
        demandHashMap.put(demand.getFromCountry().name, demand);
    }

    public void addOutgoingDemand(Demand demand){
        outgoingDemands.put(demand.getToCountry().name,demand);
    }

    public void removeOutgoingDemand(Demand demand){
        outgoingDemands.remove(demand.getToCountry().name);
    }

    public Demand getOutgoingDemand(String countryName){
        return outgoingDemands.get(countryName);
    }

    public List<String> getOutgoingDemands(){
        return outgoingDemands.keySet().stream().toList();
    }

    public boolean hasOutgoingDemands(){
        return !outgoingDemands.isEmpty();
    }

    public boolean hasAnyDemands() {
        return !demandHashMap.isEmpty();
    }

    public Demand getDemand(Country country){
        return demandHashMap.get(country.name);
    }

    public void removeDemand(Demand demand){
        demandHashMap.remove(demand.getFromCountry().name);
        sendMessage(Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("The demand from "))
                .append(demand.getFromCountry().nameComponent)
                .append(Component.text(" has been cancelled"))
                .build());
    }

    public List<String> getDemandCountryNames() {
        return demandHashMap.keySet().stream().toList();
    }

    public Player getPlayerLeader() {
        return playerLeader;
    }

    public void setPlayerLeader(Player player) {
        this.playerLeader = player;
    }

    public Block getBlockForProvince(Province province) {
        return province.getMaterial().block();
    }

    public Vault getVault() {
        return vault;
    }

    public boolean isAtWar(Country country) {
        return wars.contains(country);
    }

    public Stability getStability() {
        return stability;
    }

    public Relations getRelations() {
        return relations;
    }

    public void addLoanRequest(Loan loan) {
        loanRequests.put(loan.getFromCountry().getName(), loan);
    }

    public void acceptLoan(String from) {
        vault.addLoan(loanRequests.get(from));
    }

    public HashMap<String, Loan> getLoanRequests() {
        return loanRequests;
    }

    public void nextWeek(NewDay newDay) {
        getVault().calculateIncrease();
        getStability().newWeek();
        newWeek(newDay);
    }

    protected abstract void newWeek(NewDay newDay);

    public void nextDay(NewDay newDay){
        List<String> toRemove = new ArrayList<>();
         completedWarJustifications.forEach((name,warJust)->{
            warJust.minusExpires(1f);
            if (warJust.getExpires()<=0f){
                toRemove.add(warJust.getAgainstCountry().name);
                EventDispatcher.call(new WarJustificationExpiresEvent(warJust,this));
            }
        });
         toRemove.forEach(completedWarJustifications::remove);

         toRemove.clear();
         warJustificationHashMap.forEach((name,warJust)->{
            warJust.minusTimeLeft(1f);
            if (warJust.getTimeLeft()<=0f){
                toRemove.add(warJust.getAgainstCountry().name);
                completedWarJustifications.put(warJust.getAgainstCountry().name,warJust);
                EventDispatcher.call(new WarJustificationCompletionEvent(warJust,this));
            }
         });
         toRemove.forEach(warJustificationHashMap::remove);
        newDay(newDay);
    }

    public abstract void newDay(NewDay newDay);

    public List<Province> getCities(){
        return cities;
    }

    public void addWarJustification(WarJustification warJustification){
        warJustificationHashMap.put(warJustification.getAgainstCountry().name,warJustification);
    }

    public void removeWarJustification(String country){
        warJustificationHashMap.remove(country);
    }

    public void removeCompletedWarJustification(String country){
        completedWarJustifications.remove(country);
    }

    public List<String> getWarJustifications(){
        return warJustificationHashMap.keySet().stream().toList();
    }

    public List<String> getCompletedWarJustifications(){
        return completedWarJustifications.keySet().stream().toList();
    }

    public WarJustification getCreatingWarJustificationAgainst(String name){
        return warJustificationHashMap.get(name);
    }

    public WarJustification getCompletedWarJustificationAgainst(Country country){
        return completedWarJustifications.get(country.name);
    }

    public void addNonAggressionPact(NonAggressionPact nonAggressionPact, Country other){
        nonAggressionPactHashMap.put(other.name,nonAggressionPact);
    }

    public void removeNonAggressionPact(Country other){
        nonAggressionPactHashMap.remove(other.name);
    }

    public List<String> getNonAggressionPacts(){
        return nonAggressionPactHashMap.keySet().stream().toList();
    }

    public boolean hasNonAggressionPact(String country){
        return nonAggressionPactHashMap.containsKey(country);
    }

    public boolean isPuppet(Country country){
        return puppets.contains(country);
    }

    public boolean canFight(Country country){
        return !isFriend(country);
    }

    public boolean isFriend(Country country){
        return isPuppet(country)||isAlly(country)||hasNonAggressionPact(country.getName())||country==this;

    }

    public boolean isInAWar(){
        return !wars.isEmpty();
    }

    public void addOthersCores(Country country, Province province){
        List<Province> p = occupiesThereCores.getOrDefault(country.getName(),new ArrayList<>());
        p.add(province);
        occupiesThereCores.put(country.getName(),p);
    }

    public void removeOthersCores(Country country, Province province){
        List<Province> p = occupiesThereCores.getOrDefault(country.getName(),new ArrayList<>());
        p.remove(province);
        if (p.isEmpty()){
            occupiesThereCores.remove(country.getName());
        }else {
            occupiesThereCores.put(country.getName(),p);
        }
    }

    public List<Province> getOthersCores(Country country){
        return occupiesThereCores.get(country.getName());
    }

    public boolean occupiesCoresFrom(Country country){
        return occupiesThereCores.containsKey(country.getName());
    }

    public boolean occupiesAnyOtherCores(){
        return !occupiesThereCores.isEmpty();
    }

    public void setCapitulated(boolean opt){
        capitulated=opt;
    }

    public List<String> getOtherCountriesOccupier(){
        return occupiesThereCores.keySet().stream().toList();
    }

    public boolean hasCapitulated(){
        return capitulated;
    }

    public CompletionBarTextDisplay getCapitulationTextBar(){
        return capitulationTextBar;
    }
}
