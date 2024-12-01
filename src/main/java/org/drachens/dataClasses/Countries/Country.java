package org.drachens.dataClasses.Countries;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import net.minestom.server.utils.PacketUtils;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.bossbars.CapitulationBar;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Diplomacy.Relations;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.Loan;
import org.drachens.dataClasses.Economics.Stability;
import org.drachens.dataClasses.Economics.Vault;
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
import org.drachens.temporary.scoreboards.country.DefaultCountryScoreboard;
import org.drachens.util.AStarPathfinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.KyoriUtil.*;
import static org.drachens.util.Messages.broadcast;

public abstract class Country implements Cloneable {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    private final MapGen mapGen;
    private final List<CPlayer> players;
    private final Vault vault;
    private final List<Material> city = new ArrayList<>();
    private final HashMap<BuildingEnum, List<Building>> buildTypesListHashMap = new HashMap<>();
    private final List<Country> wars = new ArrayList<>();
    private final Ideology ideology;
    private final Election elections;
    private final List<Region> region = new ArrayList<>();
    private final List<Modifier> modifiers = new ArrayList<>();
    private final List<Country> puppets = new ArrayList<>();
    private final HashMap<Province, Material> majorCityBlocks = new HashMap<>();
    private final AStarPathfinder aStarPathfinder;
    private final Instance instance;
    private final List<Clientside> clientsides = new ArrayList<>();
    private final List<Player> playerInvites = new ArrayList<>();
    private final List<String> factionInvites = new ArrayList<>();
    private final HashMap<Country, Demand> demandHashMap = new HashMap<>();
    private final List<String> demandCountryNames = new ArrayList<>();
    private final HashMap<CurrencyTypes, CurrencyBoost> economyBoosts = new HashMap<>();
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private Player playerLeader;
    private Leader leader;
    private EconomyFactionType economyFactionType;
    private MilitaryFactionType militaryFactionType;
    private List<Province> occupies;
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
    private float maxBuildingSlotBoost = 1f;
    private float capitulationBoostPercentage = 1f;
    private Country overlord = null;
    private float relationsBoost = 0f;
    private float totalProductionBoost = 1f;
    private float stabilityGainBoost = 1f;
    private float weeklyStabilityGain = 0f;
    private final Stability stability = new Stability(50f,this);
    private final Relations relations = new Relations(this);
    private final HashMap<String,Loan> loanRequests = new HashMap<>();
    private final CapitulationBar capitulationBar = new CapitulationBar();

    public Country(HashMap<CurrencyTypes, Currencies> startingCurrencies, String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Election election, Instance instance) {
        this.occupies = new ArrayList<>();
        this.vault = new Vault(this, startingCurrencies);
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
        aStarPathfinder = new AStarPathfinder(ContinentalManagers.world(instance).provinceManager());
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
        modifier.getCurrencyBoostList().forEach(this::minusBoost);
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
        this.capitulationPoints += this.city.indexOf(city.getMaterial());
        this.cities.add(city);
    }

    public void removeCity(Province city) {
        this.capitulationPoints += this.city.indexOf(city.getMaterial());
        this.cities.remove(city);
    }

    public List<Province> getOccupies() {
        return occupies;
    }

    public void addOccupied(Province province) {
        occupies.add(province);
    }

    public void removeOccupied(Province province) {
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
    }

    Component countryJoin = getCountryMessages("countryJoin");
    Component getCountryJoin2 = getCountryMessages("broadcastedCountryJoin");
    Component getCountryLeave = getCountryMessages("countryLeave");
    public void addPlayer(CPlayer p) {
        EventDispatcher.call(new CountryJoinEvent(this, p));
        capitulationBar.addPlayer(p);
        this.players.add(p);
        p.sendMessage(mergeComp(getPrefixes("country"), replaceString(countryJoin, "%country%", this.name)));
        broadcast(mergeComp(getPrefixes("country"), replaceString(replaceString(getCountryJoin2, "%country%", this.name), "%player%", p.getUsername())), p.getInstance());
        p.teleport(capital.getPos().add(0, 1, 0));
        scoreboardManager.openScoreboard(new DefaultCountryScoreboard(),p);
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
        p.sendMessage(mergeComp(getPrefixes("country"), replaceString(getCountryLeave, "%country%", this.name)));
        clientsides.forEach(clientside -> clientside.removeViewer(p));
        if (isPlayerLeader(p)) {
            if (players.isEmpty()) {
                setPlayerLeader(null);
            } else
                setPlayerLeader(players.getFirst());
        }
        demandManager.removeActive(p);
        onRemovePlayer(p);
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
                broadcast(mergeComp(getPrefixes("country"), compBuild(attacker.name + " has seized the " + name + " capital", NamedTextColor.RED)), capital.getInstance());
            }
        }
        float capPercentage = bound(0.8f * capitulationBoostPercentage);
        float capPoints = maxCapitulationPoints * capPercentage;
        capitulationBar.setProgress(capitulationPoints/capPoints);
        if (capitulationPoints >= capPoints && !capitulated) {
            capitulated = true;
            capitulate(attacker);
        }
    }

    private float bound(float d){
        if (d>1)return 1;
        if (d<0)return 0;
        return d;
    }

    public void calculateIncrease() {
        vault.calculateIncrease();
    }

    public float getTotalProductionBoost() {
        return totalProductionBoost;
    }

    public void capitulate(Country attacker) {
        broadcast(mergeComp(getPrefixes("country"), compBuild(this.name + " has capitulated to " + attacker.name, NamedTextColor.RED)), capital.getInstance());
        for (Province p : new ArrayList<>(this.occupies)) {
            p.setOccupier(attacker);
        }
        wars.forEach(aggressor -> EventDispatcher.call(new EndWarEvent(aggressor, this)));
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

    public void minusThenLoan(Payment payment, Country from){
        getVault().minusThenLoan(payment,from);
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

    public void addWar(Country country) {
        wars.add(country);
    }

    public void removeWar(Country country) {
        wars.remove(country);
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

        createInfo();
    }

    public void addBoost(CurrencyBoost currencyBoost) {
        if (economyBoosts.containsKey(currencyBoost.getCurrencyTypes())) {
            economyBoosts.get(currencyBoost.getCurrencyTypes()).addBoost(currencyBoost.getBoost());
        } else {
            CurrencyBoost currencyBoost1 = new CurrencyBoost(currencyBoost);
            economyBoosts.put(currencyBoost1.getCurrencyTypes(), currencyBoost1);
        }
    }

    public void minusBoost(CurrencyBoost currencyBoost) {
        CurrencyTypes c = currencyBoost.getCurrencyTypes();
        if (economyBoosts.containsKey(c)) {
            economyBoosts.get(c).addBoost(currencyBoost.getBoost());
        } else {
            CurrencyBoost cb = new CurrencyBoost(c, 0f);
            economyBoosts.put(c, cb);
            economyBoosts.get(c).minusBoost(currencyBoost.getBoost());
        }
    }

    public void createInfo() {
        if (mapGen.isGenerating(instance)) return;
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

    public Country getOverlord() {
        return overlord;
    }

    public void setOverlord(Country country) {
        this.overlord = country;
    }

    public HashMap<CurrencyTypes, CurrencyBoost> getEconomyBoosts() {
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
        demandHashMap.put(demand.getFromCountry(), demand);
        demandCountryNames.add(demand.getFromCountry().name);
    }

    public boolean hasAnyDemands() {
        return !demandHashMap.isEmpty();
    }

    public boolean hasDemand(Country country) {
        return demandHashMap.containsKey(country);
    }

    public List<String> getDemandCountryNames() {
        return demandCountryNames;
    }

    public Player getPlayerLeader() {
        return playerLeader;
    }

    public void setPlayerLeader(Player player) {
        this.playerLeader = player;
    }

    public void reloadBlocksForPlayer(Player p) {
        occupies.forEach(province -> PacketUtils.sendPacket(p, new BlockChangePacket(province.getPos(), province.getMaterial().block())));
    }

    public Block getBlockForProvince(Province province) {
        return province.getMaterial().block();
    }

    public Vault getVault() {
        return vault;
    }

    public float getEconomyBoost(CurrencyTypes currencyTypes) {
        return economyBoosts.getOrDefault(currencyTypes, new CurrencyBoost(currencyTypes, 0)).getBoost() + totalProductionBoost;
    }

    public boolean isAtWar(Country country){
        return wars.contains(country);
    }

    public float getStabilityGainBoost(){
        return stabilityGainBoost;
    }

    public float getWeeklyStabilityGain(){
        return weeklyStabilityGain;
    }

    public Stability getStability(){
        return stability;
    }
    public Relations getRelations(){
        return relations;
    }
    public float getRelationsBoost(){
        return relationsBoost;
    }
    public void addLoanRequest(Loan loan){
        loanRequests.put(loan.getFromCountry().getName(),loan);
    }
    public void acceptLoan(String from){
        vault.addLoan(loanRequests.get(from));
    }
    public HashMap<String, Loan> getLoanRequests(){
        return loanRequests;
    }
}
