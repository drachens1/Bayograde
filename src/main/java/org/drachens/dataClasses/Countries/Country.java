package org.drachens.dataClasses.Countries;

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
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.bossbars.CapitulationBar;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.dataClasses.Diplomacy.PuppetChat;
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
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.EventsRunner;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.additional.ModifierCommand;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.CompletionBarTextDisplay;
import org.drachens.dataClasses.other.TextDisplay;
import org.drachens.events.NewDay;
import org.drachens.events.countries.CountryChangeEvent;
import org.drachens.events.countries.CountryJoinEvent;
import org.drachens.events.countries.CountryLeaveEvent;
import org.drachens.events.countries.war.CapitulationEvent;
import org.drachens.events.countries.warjustification.WarJustificationCompletionEvent;
import org.drachens.events.countries.warjustification.WarJustificationExpiresEvent;
import org.drachens.interfaces.MapGen;
import org.drachens.player_types.CPlayer;
import org.drachens.temporary.scoreboards.country.DefaultCountryScoreboard;
import org.drachens.util.AStarPathfinderXZ;
import org.drachens.util.MessageEnum;

import java.util.*;

import static org.drachens.util.Messages.broadcast;

public abstract class Country implements Cloneable {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    private final MapGen mapGen;
    private final List<CPlayer> players;
    private final Vault vault;
    private final List<Material> city = new ArrayList<>();
    private final HashMap<BuildingEnum, List<Building>> buildTypesListHashMap = new HashMap<>();
    private final HashSet<String> countryWars = new HashSet<>();
    private final Ideology ideology;
    private final Election elections;
    private final HashMap<String, ModifierCommand> modifierCommandsHashMap = new HashMap<>();
    private final HashMap<String, Modifier> modifiers = new HashMap<>();
    private final List<Modifier> visibleModifiers = new ArrayList<>();
    private final List<EventsRunner> eventsRunners = new ArrayList<>();
    private final List<Country> puppets = new ArrayList<>();
    private final HashMap<Province, Material> majorCityBlocks = new HashMap<>();
    private final AStarPathfinderXZ aStarPathfinder;
    private final Instance instance;
    private final List<Clientside> clientsides = new ArrayList<>();
    private final List<Player> playerInvites = new ArrayList<>();
    private final List<String> factionInvites = new ArrayList<>();
    private final HashMap<String, Demand> demandHashMap = new HashMap<>();
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private final List<Province> occupies;
    private final List<Province> cities;
    private final Stability stability;
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
    private final HashMap<String, List<Province>> occupiesThereCores = new HashMap<>();
    private final CountryChat countryChat;
    private final HashSet<ConditionEnum> conditionEnums = new HashSet<>();
    private final HashMap<String, LawCategory> laws = new HashMap<>();
    private final HashMap<String, List<Province>> bordersProvince = new HashMap<>();
    private final HashSet<String> bordersWars = new HashSet<>();
    private final HashMap<String, Integer> diplomacy = new HashMap<>();
    //1 = war 2 = neutral 3 = eco ally 4 = non aggression 5 = puppet/overlord 6 = mil ally
    private CompletionBarTextDisplay capitulationTextBar;
    private Player playerLeader;
    private Leader leader;
    private EconomyFactionType economyFactionType;
    private MilitaryFactionType militaryFactionType;
    private String name;
    private Material block;
    private Material border;
    private Province capital;
    private float capitulationPoints;
    private float maxCapitulationPoints;
    private boolean capitulated = false;
    private Component prefix;
    private Component description;
    private Country overlord = null;
    private Component originalName;
    private PuppetChat puppetChat;

    public Country(String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Election election, Instance instance, Vault vault, HashMap<String, LawCategory> laws) {
        laws.forEach(((string, lawCategory) -> this.laws.put(string, new LawCategory(lawCategory, this))));
        this.occupies = new ArrayList<>();
        this.ideology = defaultIdeologies.clone(this);
        this.vault = vault;
        vault.setCountry(this);
        this.originalName = nameComponent.clickEvent(ClickEvent.runCommand("/country info general " + name));
        this.name = name;
        setPrefix(Component.text(name, NamedTextColor.BLUE));
        this.block = block;
        this.border = border;
        this.players = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.elections = election;
        Material[] tempCities = {Material.CYAN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA, Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK};
        city.addAll(Arrays.stream(tempCities).toList());
        this.instance = instance;
        aStarPathfinder = new AStarPathfinderXZ(ContinentalManagers.world(instance).provinceManager());
        this.mapGen = ContinentalManagers.world(instance).dataStorer().votingOption.getMapGenerator();
        warsWorld = new ImaginaryWorld(instance, true);
        allyWorld = new ImaginaryWorld(instance, true);
        stability = new Stability(50f, this);
        countryChat = new CountryChat(this);
    }

    public void init() {
        List<Country> countries = new ArrayList<>(ContinentalManagers.world(instance).countryDataManager().getCountries());
        countries.remove(this);
        countries.forEach(country -> country.getOccupies().forEach(province -> {
            warsWorld.addGhostBlock(province.getPos(), Block.GRAY_CONCRETE);
            allyWorld.addGhostBlock(province.getPos(), Block.GRAY_CONCRETE);
            diplomacy.put(country.getName(), 2);
        }));
    }

    public ImaginaryWorld getWarsWorld() {
        return warsWorld;
    }

    public ImaginaryWorld getAllyWorld() {
        return allyWorld;
    }

    public void addModifierCommands(ModifierCommand modifierCommands) {
        modifierCommandsHashMap.put(modifierCommands.getString(), modifierCommands);
    }

    public void removeModifierCommands(ModifierCommand modifierCommands) {
        modifierCommandsHashMap.remove(modifierCommands.getString());
    }

    public void addModifier(Modifier modifier) {
        addModifier(modifier, false);
    }

    public void addModifier(Modifier modifier, boolean update) {
        if (modifiers.containsKey(modifier.getIdentifier())) {
            return;
        }

        modifiers.put(modifier.getIdentifier(), modifier);
        modifier.addCountry(this);
        modifier.getBoostHashMap().forEach(this::addBoost);
        modifier.getConditionEnums().forEach(this::addCondition);
        modifier.getModifierCommands().forEach(this::addModifierCommands);
        modifier.getEventsRunners().forEach(this::addEventsRunner);
        if (!update && modifier.shouldDisplay()) {
            visibleModifiers.add(modifier);
            createInfo();
        }
    }

    public void addBoost(BoostEnum boostEnum, float value) {
        float current = boostHashmap.getOrDefault(boostEnum, 1f);
        boostHashmap.put(boostEnum, value + current);
    }

    public void minusBoost(BoostEnum boostEnum, float value) {
        float current = boostHashmap.getOrDefault(boostEnum, 1f);
        boostHashmap.put(boostEnum, current - value);
    }

    public void addEventsRunner(EventsRunner eventsRunner) {
        eventsRunners.add(eventsRunner);
    }

    public void removeEventsRunner(EventsRunner eventsRunner) {
        eventsRunners.remove(eventsRunner);
    }

    public void addCondition(ConditionEnum conditionEnum) {
        conditionEnums.add(conditionEnum);
    }

    public void removeCondition(ConditionEnum conditionEnum) {
        conditionEnums.remove(conditionEnum);
    }

    public void updateModifier(Modifier modifier, Modifier old) {
        removeModifier(old, true);
        addModifier(modifier, true);
    }

    public void removeModifier(Modifier m) {
        removeModifier(m, false);
    }

    public void removeModifier(Modifier modifier, boolean update) {
        if (!modifiers.containsKey(modifier.getIdentifier())) {
            return;
        }


        modifiers.remove(modifier.getIdentifier());
        modifier.removeCountry(this);
        modifier.getBoostHashMap().forEach(this::minusBoost);
        modifier.getConditionEnums().forEach(this::removeCondition);
        modifier.getModifierCommands().forEach(this::removeModifierCommands);
        modifier.getEventsRunners().forEach(this::removeEventsRunner);
        if (!update && modifier.shouldDisplay()) {
            visibleModifiers.remove(modifier);
            createInfo();
        }
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

    public void removeCityWithoutHarm(Province province) {
        this.maxCapitulationPoints -= city.indexOf(province.getMaterial());
        this.cities.remove(province);
    }

    public List<Province> getOccupies() {
        return occupies;
    }

    public void addOccupied(Province province) {
        province.getCorers().forEach(country -> addOthersCores(country, province));
        province.getNeighbours().forEach(provinces -> {
            if (provinces.getOccupier() == this) return;
            addBorder(provinces, provinces.getOccupier().getName());
            provinces.getOccupier().addBorder(province, getName());
        });
        occupies.add(province);
    }

    public void removeOccupied(Province province) {
        province.getCorers().forEach(country -> removeOthersCores(country, province));
        province.getNeighbours().forEach(provinces -> {
            if (provinces.getOccupier() == this) return;
            removeBorder(provinces, provinces.getOccupier().getName());
            provinces.getOccupier().removeBorder(province, getName());
        });
        occupies.remove(province);
    }

    public void captureProvince(Province province) {
        warsWorld.removeGhostBlock(province.getPos());
        allyWorld.removeGhostBlock(province.getPos());
        addOccupied(province);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(Component name) {
        this.originalName = name;
    }

    public Component getNameComponent() {
        return originalName;
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
        if (capitulationTextBar != null) capitulationTextBar.getTextDisplay().dispose();
        this.capitulationTextBar = new CompletionBarTextDisplay(capital.getPos().add(0, 3, 0), capital.getInstance(), TextColor.color(0, 255, 0),Component.text(""));
    }

    public void addPlayer(CPlayer p) {
        EventDispatcher.call(new CountryJoinEvent(this, p));
        capitulationBar.addPlayer(p);
        this.players.add(p);
        p.sendMessage(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text("You have joined ", NamedTextColor.GREEN).append(originalName)).build()).build());
        broadcast(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text(p.getUsername())).append(Component.text(" has joined ", NamedTextColor.GREEN)).append(originalName).build()).build(), p.getInstance());
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
        p.sendMessage(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text("You have left ", NamedTextColor.BLUE)).append(originalName).build()).build());
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
                broadcast(Component.text()
                        .append(MessageEnum.country.getComponent())
                        .append(attacker.getNameComponent())
                        .append(Component.text(" has seized the ", NamedTextColor.RED))
                        .append(originalName)
                        .append(Component.text(" capital", NamedTextColor.RED))
                        .build(), capital.getInstance());
            }
        }
        float capPercentage = bound(0.5f * boostHashmap.getOrDefault(BoostEnum.capitulation, 1f));
        if (capPercentage >= 1f) capPercentage = 0.85f;
        float capPoints = maxCapitulationPoints * capPercentage;
        float progress = capitulationPoints / capPoints;
        capitulationTextBar.setProgress(1f - progress);
        capitulationBar.setProgress(progress);
        if (capitulationPoints >= capPoints && !capitulated) {
            EventDispatcher.call(new CapitulationEvent(this, attacker));
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
            if (p==capital){
                System.out.println("Captured the capital");
            }
            p.capture(attacker);
        }
        if (hasPuppets()) {
            puppets.forEach(puppet -> EventDispatcher.call(new CapitulationEvent(puppet, attacker)));
        }
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

    public void addClientside(Clientside clientside) {
        clientsides.add(clientside);
        players.forEach(clientside::addViewer);
        if (hasOverlord()) {
            getOverlord().addClientside(clientside);
        }
    }

    public void removeClientside(Clientside clientside) {
        clientsides.remove(clientside);
        players.forEach(clientside::removeViewer);
        if (hasOverlord()) {
            getOverlord().removeClientside(clientside);
        }
    }

    public void addANotSavedTextDisplay(TextDisplay textDisplay) {
        players.forEach(textDisplay::addViewer);
    }

    public Ideology getIdeology() {
        return ideology;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public List<String> getCountryWars() {
        return countryWars.stream().toList();
    }

    public void addCountryWar(Country country) {
        String name = country.getName();
        countryWars.add(name);
        diplomacy.put(name,1);
        country.getOccupies().forEach(province -> warsWorld.removeGhostBlock(province.getPos()));
        addClientside(country.getCapitulationTextBar().getTextDisplay());
        if (bordersProvince.containsKey(name)){
            bordersWars.add(name);
        }
    }

    public void removeWar(Country country) {
        countryWars.remove(country.getName());
        country.getOccupies().forEach(province -> warsWorld.addGhostBlock(province.getPos(), Block.GRAY_CONCRETE));
        removeClientside(country.getCapitulationTextBar().getTextDisplay());
        if (!isInAWar()) removeClientside(capitulationTextBar.getTextDisplay());
        if (bordersProvince.containsKey(name)){
            bordersWars.remove(name);
        }
        loadCountriesDiplomacy(country);
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
        if (boostEnum.isPercentage()){
            return boostHashmap.getOrDefault(boostEnum, 1f);
        }else {
            return boostHashmap.getOrDefault(boostEnum, 0f);
        }
    }

    public void createInfo() {
        if (mapGen == null || mapGen.isGenerating(instance)) return;
        List<Component> modifierComps = new ArrayList<>();
        for (Modifier modifier : visibleModifiers) {
            modifierComps.add(modifier.getName());
            modifierComps.add(Component.text(", "));
        }
        if (!modifierComps.isEmpty()) {
            modifierComps.removeLast();
        }

        Component leaderComp = Component.text()
                .append(Component.text("Faction: "))
                .build();

        List<Component> factionsComps = new ArrayList<>();
        EconomyFactionType economyFactionType1 = getEconomyFactionType();
        if (isInAnEconomicFaction()) {
            factionsComps.add(Component.text()
                    .append(Component.text("Economical faction:  "))
                    .append(economyFactionType1.getNameComponent())
                    .build());
        }
        MilitaryFactionType militaryFactionType1 = getMilitaryFactionType();
        if (isInAMilitaryFaction()) {
            factionsComps.add(Component.text()
                    .append(Component.text("Military faction: "))
                    .append(militaryFactionType1.getNameComponent())
                    .build());
        }

        List<Component> extraInfo = new ArrayList<>();
        if (hasPuppets()) {
            extraInfo.add(Component.newline());
            extraInfo.add(Component.text("Puppets: "));
            int i = 0;
            boolean showMore = false;
            for (Country country : getPuppets()) {
                if (i > 2) {
                    showMore = true;
                    extraInfo.removeLast();
                    extraInfo.add(Component.text()
                            .append(Component.text(" [CLICK]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to show the list of all the puppets", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country info puppets " + getName()))
                            .build());
                    break;
                }
                extraInfo.add(country.getNameComponent());
                extraInfo.add(Component.text(", "));
                i++;
            }
            if (!showMore) {
                extraInfo.removeLast();
            }
        } else if (hasOverlord()) {
            extraInfo.add(Component.newline());
            extraInfo.add(Component.text()
                    .append(Component.text("Overlord: "))
                    .append(getOverlord().getNameComponent())
                    .build());
        }

        if (getLeader() == null) return;
        this.description = Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(originalName)
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(getLeader().getName())
                .clickEvent(ClickEvent.runCommand("/country leader " + getName()))
                .appendNewline()
                .append(Component.text("Modifiers: "))
                .append(modifierComps)
                .appendNewline()
                .append(leaderComp)
                .append(factionsComps)
                .appendNewline()
                .append(Component.text("Ideology: "))
                .append(getIdeology().getCurrentIdeology().getModifier().getName())
                .appendNewline()
                .append(Component.text("Elections: "))
                .append(getElections().getCurrentElectionType().getName())
                .append(extraInfo)
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

    public boolean hasOverlord() {
        return overlord != null;
    }

    public void addPuppet(Country country) {
        diplomacy.put(country.getName(),5);
        if (hasOverlord()) {
            country.setOverlord(overlord);
            overlord.addPuppet(country);
        } else {
            if (puppets.isEmpty()) {
                puppetChat = new PuppetChat(this);
            }
            createInfo();
            puppets.add(country);
            countryWars.forEach(country1 -> {
                Country country2 = ContinentalManagers.world(instance).countryDataManager().getCountryFromName(country1);
                country.addCountryWar(country2);
                country2.addCountryWar(country);
            });
            country.getClientsides().forEach(this::addClientside);
        }
    }

    public void removePuppet(Country country) {
        puppets.remove(country);
        country.getClientsides().forEach(clientside -> players.forEach(clientside::removeViewer));
        if (puppets.isEmpty()) {
            puppetChat = null;
        }
        createInfo();
    }

    public List<Clientside> getClientsides() {
        return clientsides;
    }

    public Country getOverlord() {
        return overlord;
    }

    public void setOverlord(Country country) {
        this.overlord = country;
        diplomacy.put(country.getName(),5);
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
        return diplomacy.get(country.getName())==6;
    }

    public boolean isEconomicAlly(Country country) {
        return diplomacy.get(country.getName())==4;
    }

    public boolean isAlly(Country country) {
        int c = diplomacy.get(country.getName());
        return c==2||c==6;
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
        List<Building> buildings = buildTypesListHashMap.getOrDefault(building.getBuildTypes(),new ArrayList<>());
        buildings.add(building);
        buildTypesListHashMap.put(building.getBuildTypes(), buildings);
    }

    public void removeBuilding(Building building) {
        clientsides.remove(building.getItemDisplay());
        players.forEach(player -> building.getItemDisplay().removeViewer(player));
        List<Building> buildings = buildTypesListHashMap.getOrDefault(building.getBuildTypes(),new ArrayList<>());
        if (buildings.remove(building)){
            buildTypesListHashMap.put(building.getBuildTypes(), buildings);
        }else {
            buildTypesListHashMap.remove(building.getBuildTypes());
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

    public void addOutgoingDemand(Demand demand) {
        outgoingDemands.put(demand.getToCountry().name, demand);
    }

    public void removeOutgoingDemand(Demand demand) {
        outgoingDemands.remove(demand.getToCountry().name);
    }

    public Demand getOutgoingDemand(String countryName) {
        return outgoingDemands.get(countryName);
    }

    public List<String> getOutgoingDemands() {
        return outgoingDemands.keySet().stream().toList();
    }

    public boolean hasOutgoingDemands() {
        return !outgoingDemands.isEmpty();
    }

    public boolean hasAnyDemands() {
        return !demandHashMap.isEmpty();
    }

    public Demand getDemand(Country country) {
        return demandHashMap.get(country.name);
    }

    public void removeDemand(Demand demand) {
        demandHashMap.remove(demand.getFromCountry().name);
        sendMessage(Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("The demand from "))
                .append(demand.getFromCountry().originalName)
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

    public boolean isAtWar(String country) {
        return countryWars.contains(country);
    }

    public boolean isAtWar(Country country) {
        return countryWars.contains(country.getName());
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

    public void nextDay(NewDay newDay) {
        List<String> toRemove = new ArrayList<>();
        completedWarJustifications.forEach((name, warJust) -> {
            warJust.minusExpires(1f);
            if (warJust.getExpires() <= 0f) {
                toRemove.add(warJust.getAgainstCountry().name);
                EventDispatcher.call(new WarJustificationExpiresEvent(warJust, this));
            }
        });
        toRemove.forEach(completedWarJustifications::remove);

        toRemove.clear();
        warJustificationHashMap.forEach((name, warJust) -> {
            warJust.minusTimeLeft(1f);
            if (warJust.getTimeLeft() <= 0f) {
                toRemove.add(warJust.getAgainstCountry().name);
                completedWarJustifications.put(warJust.getAgainstCountry().name, warJust);
                EventDispatcher.call(new WarJustificationCompletionEvent(warJust, this));
                warJust.onFinished();
            }
        });
        toRemove.forEach(warJustificationHashMap::remove);
        newDay(newDay);

        List<EventsRunner> e = new ArrayList<>();
        new ArrayList<>(eventsRunners).forEach(eventsRunner -> {
            if (eventsRunner.newDay()) e.add(eventsRunner);
        });

        e.forEach(eventsRunners::remove);
    }

    public abstract void newDay(NewDay newDay);

    public List<Province> getCities() {
        return cities;
    }

    public void addWarJustification(WarJustification warJustification) {
        warJustificationHashMap.put(warJustification.getAgainstCountry().name, warJustification);
    }

    public void removeWarJustification(String country) {
        warJustificationHashMap.remove(country);
    }

    public void removeCompletedWarJustification(String country) {
        completedWarJustifications.remove(country);
    }

    public List<String> getWarJustifications() {
        return warJustificationHashMap.keySet().stream().toList();
    }

    public List<String> getCompletedWarJustifications() {
        return completedWarJustifications.keySet().stream().toList();
    }

    public WarJustification getCreatingWarJustificationAgainst(String name) {
        return warJustificationHashMap.get(name);
    }

    public WarJustification getCompletedWarJustificationAgainst(Country country) {
        return completedWarJustifications.get(country.name);
    }

    public void addNonAggressionPact(NonAggressionPact nonAggressionPact, Country other) {
        nonAggressionPactHashMap.put(other.name, nonAggressionPact);
        loadCountriesDiplomacy(other);
    }

    public void removeNonAggressionPact(Country other) {
        nonAggressionPactHashMap.remove(other.name);
        loadCountriesDiplomacy(other);
    }

    public List<String> getNonAggressionPacts() {
        return nonAggressionPactHashMap.keySet().stream().toList();
    }

    public boolean hasNonAggressionPact(String country) {
        return nonAggressionPactHashMap.containsKey(country);
    }

    public boolean isPuppet(Country country) {
        return puppets.contains(country);
    }

    public boolean canFight(Country country) {
        return !cantStartAWarWith(country);
    }

    public boolean isMilitaryFriend(Country country) {
        return country.getDiplomacy(country.getName())>3;
    }

    public boolean cantStartAWarWith(Country country) {
        return country.getDiplomacy(country.getName())>2;
    }

    public boolean isInAWar() {
        return !countryWars.isEmpty();
    }

    public void addOthersCores(Country country, Province province) {
        List<Province> p = occupiesThereCores.getOrDefault(country.getName(), new ArrayList<>());
        p.add(province);
        occupiesThereCores.put(country.getName(), p);
    }

    public void removeOthersCores(Country country, Province province) {
        List<Province> p = occupiesThereCores.getOrDefault(country.getName(), new ArrayList<>());
        p.remove(province);
        if (p.isEmpty()) {
            occupiesThereCores.remove(country.getName());
        } else {
            occupiesThereCores.put(country.getName(), p);
        }
    }

    public List<Province> getOthersCores(Country country) {
        return occupiesThereCores.get(country.getName());
    }

    public boolean occupiesCoresFrom(Country country) {
        return occupiesThereCores.containsKey(country.getName());
    }

    public boolean occupiesAnyOtherCores() {
        return !occupiesThereCores.isEmpty();
    }

    public void setCapitulated(boolean opt) {
        capitulated = opt;
    }

    public List<String> getOtherCountriesOccupier() {
        return occupiesThereCores.keySet().stream().toList();
    }

    public boolean hasCapitulated() {
        return capitulated;
    }

    public CompletionBarTextDisplay getCapitulationTextBar() {
        return capitulationTextBar;
    }

    public void puppet(Country overlord) {
        if (hasOverlord()) {
            getOverlord().removePuppet(this);
        } else {
            createInfo();
        }
        setOverlord(overlord);
        setBlock(overlord.getBlock());
        setBorder(overlord.getBorder());
        occupies.forEach(province -> {
            if (province.isBorder()) {
                province.setBorder();
            } else if (!province.isCity()) {
                province.setBlock();
            }
        });
    }

    public boolean hasPuppets() {
        return !puppets.isEmpty();
    }

    public boolean containsPlayer(CPlayer player) {
        return players.contains(player);
    }

    public CountryChat getCountryChat() {
        return countryChat;
    }

    public PuppetChat getPuppetChat() {
        return puppetChat;
    }

    public boolean hasCondition(ConditionEnum conditionEnum) {
        return conditionEnums.contains(conditionEnum);
    }

    public Set<String> getLawNames() {
        return laws.keySet();
    }

    public HashMap<String, LawCategory> getLaws() {
        return laws;
    }

    public LawCategory getLaw(String name) {
        return laws.get(name);
    }

    public Set<String> getModifierNames() {
        return modifierCommandsHashMap.keySet();
    }

    public ModifierCommand getModifierCommand(String identifier) {
        return modifierCommandsHashMap.get(identifier);
    }

    public Modifier getModifier(String identifier) {
        return modifiers.get(identifier);
    }

    public void addBorder(Province province, String country) {
        List<Province> p = bordersProvince.getOrDefault(country, new ArrayList<>());
        if (p.isEmpty()){
            if (isAtWar(country)){
                bordersWars.add(country);
            }
        }
        if (capital==province){
            System.out.println("Added the capital border border");
        }
        p.add(province);
        bordersProvince.put(country, p);
    }

    public void removeBorder(Province province, String country) {
        List<Province> p = bordersProvince.get(country);
        if (p.remove(province)) {
            if (capital==province){
                System.out.println("Removed the capital border");
            }
            if (p.isEmpty()) {
                bordersProvince.remove(country);
                if (isAtWar(country)){
                    bordersWars.add(country);
                }
            } else {
                bordersProvince.put(country, p);
            }
        }
    }

    public List<Province> getBordersCountry(String country) {
        return bordersProvince.get(country);
    }

    public Set<String> getBorders() {
        return bordersProvince.keySet();
    }

    public HashSet<String> getBordersWars(){
        return bordersWars;
    }

    public void loadCountriesDiplomacy(Country country){
        String name = country.getName();
        if (country.hasPuppets()){
            if (puppets.contains(country)){
                diplomacy.put(name,5);
                return;
            }
        }
        if (country.hasOverlord()){
            if (Objects.equals(name, overlord.getName())){
                diplomacy.put(name,5);
            }
        }
        if (isMilitaryAlly(country)){
            diplomacy.put(name,6);
            return;
        }
        if (isEconomicAlly(country)){
            diplomacy.put(name,3);
            return;
        }
        if (nonAggressionPactHashMap.containsKey(name)){
            diplomacy.put(name,2);
            return;
        }
        diplomacy.put(name,1);
    }

    public int getDiplomacy(String country){
        return diplomacy.getOrDefault(country,-1);
    }
}
