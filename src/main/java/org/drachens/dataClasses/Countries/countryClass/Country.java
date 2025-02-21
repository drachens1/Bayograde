package org.drachens.dataClasses.Countries.countryClass;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
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
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.dataClasses.Countries.CountryChat;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.dataClasses.Diplomacy.PuppetChat;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.Stability;
import org.drachens.dataClasses.Economics.Vault;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.Research.ResearchCountry;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.EventsRunner;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.additional.ModifierCommand;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.TextDisplay;
import org.drachens.events.NewDay;
import org.drachens.events.countries.CountryJoinEvent;
import org.drachens.events.countries.CountryLeaveEvent;
import org.drachens.events.countries.war.CapitulationEvent;
import org.drachens.generalGame.research.ResearchVault;
import org.drachens.generalGame.scoreboards.DefaultCountryScoreboard;
import org.drachens.interfaces.MapGen;
import org.drachens.interfaces.Saveable;
import org.drachens.player_types.CPlayer;
import org.drachens.util.AStarPathfinderXZ;
import org.drachens.util.MessageEnum;

import java.util.*;

import static org.drachens.util.Messages.broadcast;

@Getter
public abstract class Country implements Cloneable, Saveable {
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    private final List<Material> city;
    private final Instance instance;
    private final Info info;
    private final Research research;
    private final Diplomacy diplomacy;
    private final Economy economy;
    private final Military military;
    private final MapGen mapGen;

    public Country(String name, Component nameComponent, Material block, Material border,
                   Ideology defaultIdeologies, Instance instance,
                   Vault vault, HashMap<String, LawCategory> laws) {

        this.instance=instance;

        HashMap<String, LawCategory> tempLaws = new HashMap<>();
        laws.forEach((key, value) -> tempLaws.put(key, new LawCategory(value, this)));

        Ideology tempIdeology = defaultIdeologies.clone(this);
        Stability tempStability = new Stability(50f, this);
        CountryChat tempCountryChat = new CountryChat(this);

        ResearchCountry tempResearchCountry = null;
        ResearchVault tempResearchVault = null;
        if (ContinentalManagers.world(instance).dataStorer().votingOption.isResearchEnabled()) {
            tempResearchCountry = new ResearchCountry(this);
            tempResearchVault = new ResearchVault(tempResearchCountry);
        }

        AStarPathfinderXZ tempPathfinder = new AStarPathfinderXZ(ContinentalManagers.world(instance).provinceManager());

        this.info = new Info(name, block, border, null, 100f, 100f, false,
                Component.text(name, NamedTextColor.BLUE), Component.text("Description"),
                nameComponent.clickEvent(ClickEvent.runCommand("/country info general " + name)),
                null, null, new ArrayList<>(), instance, tempCountryChat,
                tempIdeology, tempStability);

        if (ContinentalManagers.generalManager.researchEnabled(instance)) {
            this.research = new Research(tempResearchCountry, tempResearchVault);
        }else {
            this.research = null;
        }

        this.diplomacy = new Diplomacy(new ArrayList<>(), new HashSet<>(), new HashMap<>(),
                new HashMap<>(), new HashMap<>(), new HashMap<>(),
                new HashMap<>(), new HashSet<>(), new HashMap<>(),
                new HashMap<>(), new HashMap<>(), new HashSet<>(), this);

        this.economy = new Economy(vault, null, null, new HashMap<>(), new HashMap<>(),
                null, new HashMap<>(), new HashMap<>(), new ArrayList<>(),tempLaws,new ArrayList<>());

        this.military = new Military(new ArrayList<>(), new ArrayList<>(), new HashMap<>(),
                new HashMap<>(), new HashSet<>(), new HashSet<>(),
                tempPathfinder,instance);

        Material[] tempCities = {Material.CYAN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA, Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK};
        this.city = new ArrayList<>();
        this.city.addAll(List.of(tempCities));
        this.mapGen=ContinentalManagers.world(instance).dataStorer().votingOption.getMapGenerator();
    }

    public void init() {
        List<Country> countries = new ArrayList<>(ContinentalManagers.world(instance).countryDataManager().getCountries());
        countries.remove(this);
        countries.forEach(country -> getMilitary().getOccupies().forEach(province -> {
            getMilitary().getWarsWorld().addGhostBlock(province.getPos(), Block.GRAY_CONCRETE);
            getMilitary().getAllyWorld().addGhostBlock(province.getPos(), Block.GRAY_CONCRETE);
            getDiplomacy().getDiplomacy().put(country.getName(), 2);
        }));
    }

    public String getName(){
        return getInfo().getName();
    }

    public boolean isAtWar(String country){
        return getDiplomacy().getDiplomaticRelation(country)==0;
    }

    public Component getComponentName(){
        return getInfo().getOriginalName();
    }

    public void addModifierCommands(ModifierCommand modifierCommands) {
        getEconomy().addModifierCommand(modifierCommands.getString(), modifierCommands);
    }

    public void removeModifierCommands(ModifierCommand modifierCommands) {
        getEconomy().removeModifierCommand(modifierCommands.getString());
    }

    public void addModifier(Modifier modifier) {
        addModifier(modifier, false);
    }

    public void addModifier(Modifier modifier, boolean update) {
        if (getEconomy().hasModifier(modifier.getIdentifier())) {
            return;
        }

        getEconomy().addModifier(modifier.getIdentifier(), modifier);
        modifier.addCountry(this);
        modifier.getBoostHashMap().forEach(this::addBoost);
        modifier.getConditionEnums().forEach(this::addCondition);
        modifier.getModifierCommands().forEach(this::addModifierCommands);
        modifier.getEventsRunners().forEach(this::addEventsRunner);
        if (!update && modifier.shouldDisplay()) {
            getEconomy().addVisibleModifier(modifier);
            createInfo();
        }
    }

    public void addBoost(BoostEnum boostEnum, float value) {
        getEconomy().addBoost(boostEnum,value);
    }

    public void minusBoost(BoostEnum boostEnum, float value) {
        getEconomy().removeBoost(boostEnum,value);
    }

    public void addEventsRunner(EventsRunner eventsRunner) {
        getEconomy().addEventsRunner(eventsRunner);
    }

    public void removeEventsRunner(EventsRunner eventsRunner) {
        getEconomy().removeEventsRunner(eventsRunner);
    }

    public void addCondition(ConditionEnum conditionEnum) {
        getDiplomacy().addCondition(conditionEnum);
    }

    public void removeCondition(ConditionEnum conditionEnum) {
        getDiplomacy().removeCondition(conditionEnum);
    }

    public void updateModifier(Modifier modifier, Modifier old) {
        removeModifier(old, true);
        addModifier(modifier, true);
    }

    public void removeModifier(Modifier m) {
        removeModifier(m, false);
    }

    public void removeModifier(Modifier modifier, boolean update) {
        if (!getEconomy().hasModifier(modifier.getIdentifier())) {
            return;
        }

        getEconomy().removeModifier(modifier.getIdentifier());
        modifier.removeCountry(this);
        modifier.getBoostHashMap().forEach(this::minusBoost);
        modifier.getConditionEnums().forEach(this::removeCondition);
        modifier.getModifierCommands().forEach(this::removeModifierCommands);
        modifier.getEventsRunners().forEach(this::removeEventsRunner);
        if (!update && modifier.shouldDisplay()) {
            getEconomy().removeVisibleModifier(modifier);
            createInfo();
        }
    }

    public void calculateCapitulationPercentage() {
        float points = 0;
        for (Province city : getMilitary().getCities()) {
            points += this.city.indexOf(city.getMaterial());
        }
        getInfo().setMaxCapitulationPoints(points);
        getInfo().setCapitulationPoints(0);
    }

    public void addCity(Province city) {
        getInfo().addMaxCapitulationPoints(this.city.indexOf(city.getMaterial()));
        getMilitary().addCity(city);
    }

    public void removeCity(Province city) {
        getInfo().addCapitulationPoints(this.city.indexOf(city.getMaterial()));
        getMilitary().removeCity(city);
    }

    public void removeCityWithoutHarm(Province province) {
        getInfo().minusMaxCapitulationPoints(city.indexOf(province.getMaterial()));
        getMilitary().removeCity(province);
    }

    public void addOccupied(Province province) {
        province.getCorers().forEach(country -> addOthersCores(country.getName(), province));
        province.getNeighbours().forEach(provinces -> {
            if (provinces.getOccupier() == this) return;
            addBorder(provinces, provinces.getOccupier().getName());
            provinces.getOccupier().addBorder(province, getName());
        });
        getMilitary().addOccupiedProvince(province);
    }

    public void removeOccupied(Province province) {
        province.getCorers().forEach(country -> removeOthersCores(country.getName(), province));
        province.getNeighbours().forEach(provinces -> {
            if (provinces.getOccupier() == this) return;
            removeBorder(provinces, provinces.getOccupier().getName());
            provinces.getOccupier().removeBorder(province, getName());
        });
        getMilitary().removeOccupiedProvince(province);
    }

    public void captureProvince(Province province) {
        getMilitary().getWarsWorld().removeGhostBlock(province.getPos());
        getMilitary().getAllyWorld().removeGhostBlock(province.getPos());
        addOccupied(province);
    }

    public void setCapital(Province capital) {
        getInfo().setCapital(capital);
        getEconomy().getCapitulationTextBar().getTextDisplay().setPos(capital.getPos().add(0, 3, 0));
    }

    public void addPlayer(CPlayer p) {
        EventDispatcher.call(new CountryJoinEvent(this, p));
        getMilitary().getCapitulationBar().addPlayer(p);
        getInfo().addPlayer(p);
        p.sendMessage(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text("You have joined ", NamedTextColor.GREEN).append(getInfo().getOriginalName())).build()).build());
        broadcast(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text(p.getUsername())).append(Component.text(" has joined ", NamedTextColor.GREEN)).append(getInfo().getOriginalName()).build()).build(), p.getInstance());
        p.teleport(getInfo().getCapital().getPos().add(0, 1, 0));
        scoreboardManager.openScoreboard(new DefaultCountryScoreboard(), p);
        DefaultCountryScoreboard defaultCountryScoreboard = (DefaultCountryScoreboard) scoreboardManager.getScoreboard(p);
        defaultCountryScoreboard.openEconomy();
        getInfo().getClientsides().forEach(clientside -> clientside.addViewer(p));
        if (getInfo().getPlayerLeader()==null)
            setPlayerLeader(p);
        p.refreshCommands();
        onAddPlayer(p);
    }

    protected abstract void onAddPlayer(CPlayer p);

    public void removePlayer(CPlayer p) {
        EventDispatcher.call(new CountryLeaveEvent(this, p));
        getMilitary().getCapitulationBar().removePlayer(p);
        getInfo().removePlayer(p);
        p.sendMessage(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text("You have left ", NamedTextColor.BLUE)).append(getInfo().getOriginalName()).build()).build());
        getInfo().getClientsides().forEach(clientside -> clientside.removeViewer(p));
        if (isPlayerLeader(p)) {
            if (getInfo().getPlayers().isEmpty()) {
                setPlayerLeader(null);
            } else
                setPlayerLeader(getInfo().getPlayers().getFirst());
        }
        demandManager.removeActive(p.getCountry());
        onRemovePlayer(p);
        getMilitary().getWarsWorld().removePlayer(p);
        getMilitary().getAllyWorld().removePlayer(p);
        p.refreshCommands();
        p.setCountry(null);
    }

    protected abstract void onRemovePlayer(CPlayer p);

    public void cityCaptured(Country attacker, Province capturedCity) {
        removeCity(capturedCity);
        if (!getInfo().isCapitulated()) {
            if (getInfo().getCapital() == capturedCity) {
                broadcast(Component.text()
                        .append(MessageEnum.country.getComponent())
                        .append(attacker.getInfo().getOriginalName())
                        .append(Component.text(" has seized the ", NamedTextColor.RED))
                        .append(getInfo().getOriginalName())
                        .append(Component.text(" capital", NamedTextColor.RED))
                        .build(), getInfo().getInstance());
            }
        }
        float capPercentage = bound(0.5f * getEconomy().getBoost(BoostEnum.capitulation));
        if (capPercentage >= 1f) capPercentage = 0.85f;
        float capPoints = getInfo().getMaxCapitulationPoints() * capPercentage;
        float progress = getInfo().getMaxCapitulationPoints() / capPoints;
        getEconomy().getCapitulationTextBar().setProgress(1f - progress);
        getEconomy().getCapitulationTextBar().setProgress(progress);
        if (getInfo().getMaxCapitulationPoints() >= capPoints && !getInfo().isCapitulated()) {
            EventDispatcher.call(new CapitulationEvent(this, attacker));
        }
    }

    private float bound(float d) {
        if (d > 1) return 1;
        if (d < 0) return 0;
        return d;
    }

    public void capitulate(Country attacker) {
        getInfo().setCapitulated(true);
        for (Province p : new ArrayList<>(getMilitary().getOccupies())) {
            if (p==getInfo().getCapital()){
                System.out.println("Captured the capital");
            }
            p.capture(attacker);
        }
        if (!getDiplomacy().getPuppets().isEmpty()) {
            getDiplomacy().getPuppets().forEach(puppet -> EventDispatcher.call(new CapitulationEvent(puppet, attacker)));
        }
    }

    public void addPayment(Payment payment, Component msg) {
        sendMessage(msg);
        getEconomy().getVault().addPayment(payment);
    }

    public void addPayments(Payments payments) {
        getEconomy().getVault().addPayments(payments);
    }

    public void removePayments(Payments payments) {
        getEconomy().getVault().minusPayments(payments);
    }

    public void removePayment(Payment payment) {
        getEconomy().getVault().minusPayment(payment);
    }

    public float subtractMaximumAmountPossible(Payment payment) {
        return getEconomy().getVault().subtractMaxAmountPossible(payment);
    }

    public boolean canMinusCost(Payment cost) {
        return getEconomy().getVault().canMinus(cost);
    }

    public boolean canMinusCosts(Payments cost) {
        return getEconomy().getVault().canMinus(cost);
    }

    public void minusThenLoan(Payment payment, Country from) {
        getEconomy().getVault().minusThenLoan(payment, from);
    }

    public void addClientside(Clientside clientside) {
        getInfo().addClientside(clientside);
        if (hasOverlord()) {
            getInfo().getOverlord().addClientside(clientside);
        }
    }

    public void removeClientside(Clientside clientside) {
        getInfo().removeClientside(clientside);
        if (hasOverlord()) {
            getInfo().getOverlord().removeClientside(clientside);
        }
    }

    public void addANotSavedTextDisplay(TextDisplay textDisplay) {
        getInfo().getPlayers().forEach(textDisplay::addViewer);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void addCountryWar(Country country) {
        String name = country.getName();
        country.getMilitary().addCountryWar(name);
        country.getDiplomacy().addDiplomaticRelation(name,1);
        country.getMilitary().getOccupies().forEach(province -> country.getMilitary().getWarsWorld().removeGhostBlock(province.getPos()));
        addClientside(country.getEconomy().getCapitulationTextBar().getTextDisplay());
        if (getDiplomacy().getBordersProvince().containsKey(name)){
            getMilitary().addBorderWar(name);
        }
    }

    public void removeWar(Country country) {
        String name = country.getName();
        country.getMilitary().removeCountryWar(name);
        country.getMilitary().getOccupies().forEach(province -> country.getMilitary().getWarsWorld().addGhostBlock(province.getPos(),Block.GRAY_CONCRETE));
        removeClientside(country.getEconomy().getCapitulationTextBar().getTextDisplay());
        if (!isInAWar()) removeClientside(country.getEconomy().getCapitulationTextBar().getTextDisplay());
        if (getDiplomacy().getBordersProvince().containsKey(name)){
            getMilitary().removeBorderWar(name);
        }
        loadCountriesDiplomacy(country);
    }

    public boolean hasPuppets(){
        return !getDiplomacy().getPuppets().isEmpty();
    }

    public boolean isInAWar(){
        return getDiplomacy().getCountryWars().isEmpty();
    }

    public Component getPrefix() {
        return getInfo().getPrefix();
    }

    public void sendMessage(Component msg) {
        for (Player p : getInfo().getPlayers()) {
            p.sendMessage(msg);
        }
    }

    public void sendActionBar(Component msg) {
        for (Player p : getInfo().getPlayers()) {
            p.sendActionBar(msg);
        }
    }

    public void setLeader(Leader leader) {
        getInfo().setLeader(leader);
        leader.getModifier().forEach((this::addModifier));
    }

    public float getBoost(BoostEnum boostEnum) {
        return getEconomy().getBoost(boostEnum);
    }

    public boolean hasOverlord(){
        return getInfo().getOverlord()!=null;
    }

    public void createInfo() {
        if (mapGen == null || mapGen.isGenerating(instance)) return;
        List<Component> modifierComps = new ArrayList<>();
        for (Modifier modifier : getEconomy().getVisibleModifiers()) {
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
        EconomyFactionType economyFactionType1 = getEconomy().getEconomyFactionType();
        if (isInAnEconomicFaction()) {
            factionsComps.add(Component.text()
                    .append(Component.text("Economical faction:  "))
                    .append(economyFactionType1.getNameComponent())
                    .build());
        }
        MilitaryFactionType militaryFactionType1 = getEconomy().getMilitaryFactionType();
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
            for (Country country : getDiplomacy().getPuppets()) {
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
                extraInfo.add(country.getInfo().getOriginalName());
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
                    .append(getInfo().getOverlord().getInfo().getOriginalName())
                    .build());
        }

        if (getInfo().getLeader() == null) return;
        this.getInfo().setDescription(Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(getInfo().getOriginalName())
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(getInfo().getLeader().getName())
                .clickEvent(ClickEvent.runCommand("/country leader " + getName()))
                .appendNewline()
                .append(Component.text("Modifiers: "))
                .append(modifierComps)
                .appendNewline()
                .append(leaderComp)
                .append(factionsComps)
                .appendNewline()
                .append(Component.text("Ideology: "))
                .append(getInfo().getIdeology().getCurrentIdeology().getModifier().getName())
                .appendNewline()
                .append(extraInfo)
                .build());
    }

    public void addPuppet(Country country) {
        getDiplomacy().addDiplomaticRelation(country.getName(), 5);
        if (hasOverlord()) {
            Country overlord = country.getInfo().getOverlord();
            country.setOverlord(overlord);
            overlord.addPuppet(country);
        } else {
            if (!hasPuppets()) {
                country.getInfo().setPuppetChat(new PuppetChat(this));
            }
            createInfo();
            PuppetChat puppetChat = country.getInfo().getPuppetChat();
            country.getInfo().getPlayers().forEach(puppetChat::addPlayer);
            country.getDiplomacy().addPuppet(country);
            country.getDiplomacy().getCountryWars().forEach(country1 -> {
                Country country2 = ContinentalManagers.world(instance).countryDataManager().getCountryFromName(country1);
                country.addCountryWar(country2);
                country2.addCountryWar(country);
            });
            country.getInfo().getClientsides().forEach(this::addClientside);
        }
    }

    public void removePuppet(Country country) {
        country.getDiplomacy().removePuppet(country);
        country.getInfo().getClientsides().forEach(clientside -> getInfo().getPlayers().forEach(clientside::removeViewer));
        if (hasPuppets()) {
            getInfo().setPuppetChat(null);
        }
        createInfo();
    }

    public void setOverlord(Country country) {
        getInfo().setOverlord(country);
        getDiplomacy().addDiplomaticRelation(country.getName(),5);
    }

    public void endGame() {
        //aiCompetitor.kill();
        getInfo().getClientsides().forEach(Clientside::dispose);
    }

    public boolean canJoinFaction(Faction faction) {
        return (faction instanceof MilitaryFactionType && getEconomy().getMilitaryFactionType() == null) || (faction instanceof EconomyFactionType && getEconomy().getEconomyFactionType() == null);
    }

    public boolean isLeaderOfAFaction() {
        return isEconomyFactionLeader() || isMilitaryFactionLeader();
    }

    public boolean isEconomyFactionLeader() {
        return getEconomy().getEconomyFactionType() != null && getEconomy().getEconomyFactionType().getLeader() == this;
    }

    public boolean isMilitaryFactionLeader() {
        return getEconomy().getMilitaryFactionType() != null && getEconomy().getMilitaryFactionType().getLeader() == this;
    }

    public boolean isMilitaryAlly(Country country) {
        return getDiplomacy().getDiplomaticRelation(country.getName())==6;
    }

    public boolean isEconomicAlly(Country country) {
        return getDiplomacy().getDiplomaticRelation(country.getName())==4;
    }

    public boolean isAlly(Country country) {
        int c = getDiplomacy().getDiplomaticRelation(country.getName());
        return c==2||c==6;
    }

    public boolean isInAFaction() {
        return isInAMilitaryFaction() || isInAnEconomicFaction();
    }

    public boolean isInAMilitaryFaction() {
        return getEconomy().getMilitaryFactionType() != null;
    }

    public boolean isInAnEconomicFaction() {
        return getEconomy().getEconomyFactionType() != null;
    }

    public boolean isInAllFactions() {
        return !isInAMilitaryFaction() && !isInAnEconomicFaction();
    }

    public void addBuilding(Building building) {
        getInfo().addClientside(building.getItemDisplay());
        building.getItemDisplay().addCountry(this);
        getEconomy().addBuilding(building);
    }

    public void removeBuilding(Building building) {
        getInfo().removeClientside(building.getItemDisplay());
        building.getItemDisplay().removeCountry(this);
        getEconomy().removeBuilding(building);
    }

    public boolean isPlayerLeader(Player player) {
        return getInfo().getPlayerLeader() == player;
    }

    public void removeDemand(Demand demand,String name) {
        getDiplomacy().removeDemand(name);
        sendMessage(Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("The demand from "))
                .append(demand.getFromCountry().getInfo().getOriginalName())
                .append(Component.text(" has been cancelled"))
                .build());
    }

    public void setPlayerLeader(CPlayer player) {
        if (getInfo().getPlayerLeader()!=null)getInfo().getPlayerLeader().refreshCommands();
        getInfo().setPlayerLeader(player);
    }

    public void nextWeek(NewDay newDay) {
        getEconomy().getVault().calculateIncrease();
        if (getResearch()!=null){
            getResearch().researchVault().extraCalcIncrease();
            getResearch().researchCountry().newWeek(newDay);
        }
        getInfo().getStability().newWeek();
        newWeek(newDay);
    }

    protected abstract void newWeek(NewDay newDay);

    public void nextDay(NewDay newDay) {
        List<EventsRunner> e = new ArrayList<>();
        getEconomy().getEventsRunners().forEach(eventsRunner -> {
            if (eventsRunner.newDay()) e.add(eventsRunner);
        });

        getEconomy().removeEventsRunners(e);
        newDay(newDay);
    }

    public abstract void newDay(NewDay newDay);

    public void addNonAggressionPact(NonAggressionPact nonAggressionPact, Country other) {
        getDiplomacy().addNonAggressionPact(other.getName(),nonAggressionPact);
        loadCountriesDiplomacy(other);
    }

    public void removeNonAggressionPact(Country other) {
        getDiplomacy().removeNonAggressionPact(other.getName());
        loadCountriesDiplomacy(other);
    }

    public boolean canFight(Country country) {
        return !cantStartAWarWith(country);
    }

    public int getDiplomaticRelations(String country){
        return getDiplomacy().getDiplomaticRelation(country);
    }

    public boolean isMilitaryFriend(Country country) {
        return country.getDiplomaticRelations(country.getName())>3;
    }

    public boolean cantStartAWarWith(Country country) {
        return country.getDiplomaticRelations(country.getName())>2;
    }

    public void addOthersCores(String country, Province province) {
        getMilitary().addOthersCoreProvince(country,province);
    }

    public void removeOthersCores(String country, Province province) {
        getMilitary().removeOthersCoreProvince(country,province);
    }

    public Ideology getIdeology(){
        return getInfo().getIdeology();
    }

    public void puppet(Country overlord) {
        if (hasOverlord()) {
            getInfo().getOverlord().removePuppet(this);
        } else {
            createInfo();
        }
        setOverlord(overlord);
        getInfo().setBlock(overlord.getInfo().getBlock());
        getInfo().setBorder(overlord.getInfo().getBorder());
        getMilitary().getOccupies().forEach(province -> {
            if (province.isBorder()) {
                province.setBorder();
            } else if (!province.isCity()) {
                province.setBlock();
            }
        });
    }

    public void addBorder(Province province, String country) {
        getMilitary().addBorder(country,province);
    }

    public void removeBorder(Province province, String country) {
        getMilitary().removeBorder(country,province);
    }

    public void loadCountriesDiplomacy(Country country){
        String name = country.getName();
        if (country.hasPuppets()){
            if (getDiplomacy().containsPuppet(country)){
                getDiplomacy().addDiplomaticRelation(name,5);
                return;
            }
        }
        if (country.hasOverlord()){
            if (Objects.equals(name, getInfo().getOverlord().getName())){
                getDiplomacy().addDiplomaticRelation(name,5);
            }
        }
        if (isMilitaryAlly(country)){
            getDiplomacy().addDiplomaticRelation(name,6);
            return;
        }
        if (isEconomicAlly(country)){
            getDiplomacy().addDiplomaticRelation(name,3);
            return;
        }
        if (getDiplomacy().containsNonAggressionPact(name)){
            getDiplomacy().addDiplomaticRelation(name,2);
            return;
        }
        getDiplomacy().addDiplomaticRelation(name,1);
    }

    public JsonElement getReference(){
        return new JsonPrimitive(getName());
    }

    @Override
    public JsonElement toJson() {
        return new JsonObject();
    }

    public Block getBlockForProvince(Province province) {
        return province.getMaterial().block();
    }

    protected abstract JsonElement abstractToJson();
}
