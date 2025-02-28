package org.drachens.dataClasses.Countries.countryClass;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import lombok.Setter;
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
import org.drachens.Manager.defaults.enums.ColoursEnum;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.dataClasses.Countries.CountryChat;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Countries.Leader;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
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
import org.drachens.dataClasses.other.CompletionBarTextDisplay;
import org.drachens.dataClasses.other.TextDisplay;
import org.drachens.events.NewDay;
import org.drachens.events.countries.CountryJoinEvent;
import org.drachens.events.countries.CountryLeaveEvent;
import org.drachens.events.countries.war.CapitulationEvent;
import org.drachens.generalGame.research.ResearchVault;
import org.drachens.generalGame.scoreboards.DefaultCountryScoreboard;
import org.drachens.generalGame.scoreboards.DefaultScoreboard;
import org.drachens.interfaces.MapGen;
import org.drachens.interfaces.Saveable;
import org.drachens.interfaces.ai.AI;
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
    private final String name;
    @Setter
    private AI ai;

    protected Country(String name, Component nameComponent, Material block, Material border,
                      Ideology defaultIdeologies, Instance instance,
                      Vault vault, HashMap<String, LawCategory> laws) {
        this.diplomacy = new Diplomacy(new ArrayList<>(), new HashMap<>(),
                new HashMap<>(), new HashMap<>(),
                new HashMap<>(), new HashSet<>(), new HashMap<>(),
                new HashMap<>(), new HashMap<>(), new HashSet<>());

        this.name=name;

        HashMap<String, LawCategory> tempLaws = new HashMap<>();
        laws.forEach((key, value) -> tempLaws.put(key, new LawCategory(value, this)));
        this.economy = new Economy(vault, null, null, new HashMap<>(), new HashMap<>(),
                null, new HashMap<>(), new HashMap<>(), new ArrayList<>(),tempLaws,new ArrayList<>());

        AStarPathfinderXZ tempPathfinder = new AStarPathfinderXZ(ContinentalManagers.world(instance).provinceManager());

        this.military = new Military(new ArrayList<>(), new ArrayList<>(), new HashMap<>(),
                new HashMap<>(), new HashSet<>(), new HashSet<>(),
                tempPathfinder,instance);

        Material[] tempCities = {Material.CYAN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA, Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK};

        this.city = new ArrayList<>();
        this.city.addAll(List.of(tempCities));
        this.mapGen =ContinentalManagers.world(instance).dataStorer().votingOption.getMapGenerator();

        if (ContinentalManagers.generalManager.researchEnabled(instance)) {
            ResearchCountry tempResearchCountry = new ResearchCountry(this);
            this.research = new Research(tempResearchCountry, new ResearchVault(tempResearchCountry));
        } else {
            this.research = null;
        }
        this.instance=instance;

        Ideology tempIdeology = defaultIdeologies.clone(this);
        CountryChat tempCountryChat = new CountryChat(this);
        this.info = new Info(name, block, border, null, 100.0f, 100.0f, false,
                Component.text(name, NamedTextColor.BLUE), Component.text("Description"),
                nameComponent.clickEvent(ClickEvent.runCommand("/country info general " + name)),
                null, null, new ArrayList<>(), instance, tempCountryChat,
                tempIdeology, new Stability(50.0f, this));
    }

    private Country(Instance instance, Info info, Research research, Diplomacy diplomacy, Economy economy, Military military){
        this.name=info.getName();
        this.info=info;
        this.instance=instance;
        this.research=research;
        this.diplomacy=diplomacy;
        this.economy=economy;
        this.military=military;
        Material[] tempCities = {Material.CYAN_GLAZED_TERRACOTTA, Material.GREEN_GLAZED_TERRACOTTA, Material.LIME_GLAZED_TERRACOTTA,
                Material.YELLOW_GLAZED_TERRACOTTA, Material.RAW_GOLD_BLOCK, Material.GOLD_BLOCK, Material.EMERALD_BLOCK};
        this.city = new ArrayList<>();
        this.city.addAll(List.of(tempCities));
        mapGen=null;
    }

    public void init() {
        List<Country> countries = new ArrayList<>(ContinentalManagers.world(instance).countryDataManager().getCountries());
        countries.remove(this);
        countries.forEach(country -> this.military.getOccupies().forEach(province -> {
            this.military.getWarsWorld().addGhostBlock(province.getPos(), Block.GRAY_CONCRETE);
            this.military.getAllyWorld().addGhostBlock(province.getPos(), Block.GRAY_CONCRETE);
            loadCountriesDiplomacy(country);
        }));
    }

    public boolean isAtWar(String country){
        return 0 == diplomacy.getDiplomaticRelation(country);
    }

    public Component getComponentName(){
        return this.info.getOriginalName();
    }

    public void addModifierCommands(ModifierCommand modifierCommands) {
        this.economy.addModifierCommand(modifierCommands.getString(), modifierCommands);
    }

    public void removeModifierCommands(ModifierCommand modifierCommands) {
        this.economy.removeModifierCommand(modifierCommands.getString());
    }

    public void addModifier(Modifier modifier) {
        addModifier(modifier, false);
    }

    public void addModifier(Modifier modifier, boolean update) {
        if (this.economy.hasModifier(modifier.getIdentifier())) {
            return;
        }

        this.economy.addModifier(modifier.getIdentifier(), modifier);
        modifier.addCountry(this);
        modifier.getBoostHashMap().forEach(this::addBoost);
        modifier.getConditionEnums().forEach(this::addCondition);
        modifier.getModifierCommands().forEach(this::addModifierCommands);
        modifier.getEventsRunners().forEach(this::addEventsRunner);
        if (!update && modifier.shouldDisplay()) {
            this.economy.addVisibleModifier(modifier);
            createInfo();
        }
    }

    public void addBoost(BoostEnum boostEnum, float value) {
        this.economy.addBoost(boostEnum,value);
    }

    public void minusBoost(BoostEnum boostEnum, float value) {
        this.economy.removeBoost(boostEnum,value);
    }

    public void addEventsRunner(EventsRunner eventsRunner) {
        this.economy.addEventsRunner(eventsRunner);
    }

    public void removeEventsRunner(EventsRunner eventsRunner) {
        this.economy.removeEventsRunner(eventsRunner);
    }

    public void addCondition(ConditionEnum conditionEnum) {
        this.diplomacy.addCondition(conditionEnum);
    }

    public void removeCondition(ConditionEnum conditionEnum) {
        this.diplomacy.removeCondition(conditionEnum);
    }

    public void updateModifier(Modifier modifier, Modifier old) {
        removeModifier(old, true);
        addModifier(modifier, true);
    }

    public void removeModifier(Modifier m) {
        removeModifier(m, false);
    }

    public void removeModifier(Modifier modifier, boolean update) {
        if (!this.economy.hasModifier(modifier.getIdentifier())) {
            return;
        }

        this.economy.removeModifier(modifier.getIdentifier());
        modifier.removeCountry(this);
        modifier.getBoostHashMap().forEach(this::minusBoost);
        modifier.getConditionEnums().forEach(this::removeCondition);
        modifier.getModifierCommands().forEach(this::removeModifierCommands);
        modifier.getEventsRunners().forEach(this::removeEventsRunner);
        if (!update && modifier.shouldDisplay()) {
            this.economy.removeVisibleModifier(modifier);
            createInfo();
        }
    }

    public void calculateCapitulationPercentage() {
        float points = 0;
        for (Province city : this.military.getCities()) {
            points += this.city.indexOf(city.getMaterial());
        }
        this.info.setMaxCapitulationPoints(points);
        this.info.setCapitulationPoints(0);
    }

    public void addCity(Province city) {
        this.info.addMaxCapitulationPoints(this.city.indexOf(city.getMaterial()));
        this.military.addCity(city);
    }

    public void removeCity(Province city) {
        this.info.addCapitulationPoints(this.city.indexOf(city.getMaterial()));
        this.military.removeCity(city);
    }

    public void removeCityWithoutHarm(Province province) {
        this.info.minusMaxCapitulationPoints(city.indexOf(province.getMaterial()));
        this.military.removeCity(province);
    }

    public void addOccupied(Province province) {
        province.getCorers().forEach(country -> addOthersCores(country.getName(), province));
        province.getNeighbours().forEach(provinces -> {
            if (provinces.getOccupier() == this) return;
            addBorder(provinces, provinces.getOccupier().getName());
            provinces.getOccupier().addBorder(province, getName());
        });
        this.military.addOccupiedProvince(province);
    }

    public void removeOccupied(Province province) {
        province.getCorers().forEach(country -> removeOthersCores(country.getName(), province));
        province.getNeighbours().forEach(provinces -> {
            if (provinces.getOccupier() == this) return;
            removeBorder(provinces, provinces.getOccupier().getName());
            provinces.getOccupier().removeBorder(province, getName());
        });
        this.military.removeOccupiedProvince(province);
        if (ai != null) ai.attackedAt(province);
    }

    public void captureProvince(Province province) {
        this.military.getWarsWorld().removeGhostBlock(province.getPos());
        this.military.getAllyWorld().removeGhostBlock(province.getPos());
        addOccupied(province);
    }

    public void setCapital(Province capital) {
        this.info.setCapital(capital);
        if (economy.getCapitulationTextBar()!=null){
            this.economy.getCapitulationTextBar().getTextDisplay().setPos(capital.getPos().add(0, 3, 0));
        }else {
            this.economy.setCapitulationTextBar(new CompletionBarTextDisplay(capital.getPos().add(0.5,1.5,0.5),instance, ColoursEnum.GREEN.getTextColor(),Component.text("")));
        }
    }

    public void addPlayer(CPlayer p) {
        EventDispatcher.call(new CountryJoinEvent(this, p));
        this.military.getCapitulationBar().addPlayer(p);
        this.info.addPlayer(p);
        p.sendMessage(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text("You have joined ", NamedTextColor.GREEN).append(this.info.getOriginalName())).build()).build());
        broadcast(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text(p.getUsername())).append(Component.text(" has joined ", NamedTextColor.GREEN)).append(this.info.getOriginalName()).build()).build(), p.getInstance());
        p.teleport(this.info.getCapital().getPos().add(0, 1, 0));
        scoreboardManager.openScoreboard(new DefaultCountryScoreboard(), p);
        DefaultCountryScoreboard defaultCountryScoreboard = (DefaultCountryScoreboard) scoreboardManager.getScoreboard(p);
        defaultCountryScoreboard.openEconomy();
        this.info.getClientsides().forEach(clientside -> clientside.addViewer(p));
        if (null == info.getPlayerLeader()) setPlayerLeader(p);
        p.refreshCommands();
        onAddPlayer(p);
    }

    protected abstract void onAddPlayer(CPlayer p);

    public void removePlayer(CPlayer p) {
        EventDispatcher.call(new CountryLeaveEvent(this, p));
        this.military.getCapitulationBar().removePlayer(p);
        this.info.removePlayer(p);
        p.sendMessage(Component.text().append(MessageEnum.country.getComponent()).append(Component.text().append(Component.text("You have left ", NamedTextColor.BLUE)).append(this.info.getOriginalName()).build()).build());
        this.info.getClientsides().forEach(clientside -> clientside.removeViewer(p));
        if (isPlayerLeader(p)) {
            if (this.info.getPlayers().isEmpty()) {
                setPlayerLeader(null);
            } else setPlayerLeader(this.info.getPlayers().getFirst());
        }
        demandManager.removeActive(p.getCountry());
        onRemovePlayer(p);
        this.military.getWarsWorld().removePlayer(p);
        this.military.getAllyWorld().removePlayer(p);
        ContinentalManagers.scoreboardManager.openScoreboard(new DefaultScoreboard(),p);
        p.setCountry(null);
        p.refreshCommands();
    }

    protected abstract void onRemovePlayer(CPlayer p);

    public void cityCaptured(Country attacker, Province capturedCity) {
        removeCity(capturedCity);
        if (!this.info.isCapitulated()) {
            if (this.info.getCapital() == capturedCity) {
                broadcast(Component.text()
                        .append(MessageEnum.country.getComponent())
                        .append(attacker.info.getOriginalName())
                        .append(Component.text(" has seized the ", NamedTextColor.RED))
                        .append(this.info.getOriginalName())
                        .append(Component.text(" capital", NamedTextColor.RED))
                        .build(), this.info.getInstance());
            }
        }
        float capPercentage = bound(0.5f * this.economy.getBoost(BoostEnum.capitulation));
        if (1.0f <= capPercentage) capPercentage = 0.85f;
        float capPoints = this.info.getMaxCapitulationPoints() * capPercentage;
        float progress = this.info.getMaxCapitulationPoints() / capPoints;
        this.economy.getCapitulationTextBar().setProgress(1.0f - progress);
        this.economy.getCapitulationTextBar().setProgress(progress);
        if (info.getMaxCapitulationPoints() >= capPoints && !this.info.isCapitulated()) {
            EventDispatcher.call(new CapitulationEvent(this, attacker));
        }
    }

    private float bound(float d) {
        if (1 < d) return 1;
        if (0 > d) return 0;
        return d;
    }

    public void capitulate(Country attacker) {
        this.info.setCapitulated(true);
        for (Province p : new ArrayList<>(this.military.getOccupies())) {
            if (p== this.info.getCapital()) {
                System.out.println("Captured the capital");
            }
            p.capture(attacker);
        }
        if (!this.diplomacy.getPuppets().isEmpty()) {
            this.diplomacy.getPuppets().forEach(puppet -> {
                if (!puppet.getInfo().isCapitulated()) EventDispatcher.call(new CapitulationEvent(puppet, attacker));
            });
        }
    }

    public void addPayment(Payment payment, Component msg) {
        sendMessage(msg);
        this.economy.getVault().addPayment(payment);
    }

    public void addPayments(Payments payments) {
        this.economy.getVault().addPayments(payments);
    }

    public void removePayments(Payments payments) {
        this.economy.getVault().minusPayments(payments);
    }

    public void removePayment(Payment payment) {
        this.economy.getVault().minusPayment(payment);
    }

    public float subtractMaximumAmountPossible(Payment payment) {
        return this.economy.getVault().subtractMaxAmountPossible(payment);
    }

    public boolean canMinusCost(Payment cost) {
        return economy.getVault().canMinus(cost);
    }

    public boolean canMinusCosts(Payments cost) {
        return this.economy.getVault().canMinus(cost);
    }

    public void minusThenLoan(Payment payment, Country from) {
        this.economy.getVault().minusThenLoan(payment, from);
    }

    public void addClientside(Clientside clientside) {
        info.addClientside(clientside);
        if (this.hasOverlord()) info.getOverlord().addClientside(clientside);
    }

    public void removeClientside(Clientside clientside) {
        info.removeClientside(clientside);
        if (this.hasOverlord()) info.getOverlord().removeClientside(clientside);
    }

    public void addANotSavedTextDisplay(TextDisplay textDisplay) {
        this.info.getPlayers().forEach(textDisplay::addViewer);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void addCountryWar(Country country) {
        String name = country.getName();
        military.addCountryWar(name);
        diplomacy.addDiplomaticRelation(name,0);
        military.getOccupies().forEach(province -> military.getWarsWorld().removeGhostBlock(province.getPos()));
        addClientside(country.getEconomy().getCapitulationTextBar().getTextDisplay());
        if (this.military.containsBorder(name)) {
            this.military.addBorderWar(name);
        }
    }

    public void removeWar(Country country) {
        String name = country.getName();
        military.removeCountryWar(name);
        military.getOccupies().forEach(province -> country.military.getWarsWorld().addGhostBlock(province.getPos(),Block.GRAY_CONCRETE));
        removeClientside(country.economy.getCapitulationTextBar().getTextDisplay());
        if (!isInAWar()) removeClientside(economy.getCapitulationTextBar().getTextDisplay());
        if (this.military.containsBorder(name)) {
            this.military.removeBorderWar(name);
        }
        loadCountriesDiplomacy(country);
    }

    public boolean hasPuppets(){
        return !this.diplomacy.getPuppets().isEmpty();
    }

    public boolean isInAWar(){
        return this.military.getCountryWars().isEmpty();
    }

    public Component getPrefix() {
        return info.getOriginalName();
    }

    public void sendMessage(Component msg) {
        for (Player p : this.info.getPlayers()) {
            p.sendMessage(msg);
        }
    }

    public void sendActionBar(Component msg) {
        for (Player p : this.info.getPlayers()) {
            p.sendActionBar(msg);
        }
    }

    public void setLeader(Leader leader) {
        this.info.setLeader(leader);
        leader.getModifier().forEach(this::addModifier);
    }

    public float getBoost(BoostEnum boostEnum) {
        return economy.getBoost(boostEnum);
    }

    public boolean hasOverlord(){
        return null != info.getOverlord();
    }

    public void createInfo() {
        if (null == this.mapGen || mapGen.isGenerating(instance)) return;
        List<Component> modifierComps = new ArrayList<>();
        for (Modifier modifier : this.economy.getVisibleModifiers()) {
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
        EconomyFactionType economyFactionType1 = this.economy.getEconomyFactionType();
        if (isInAnEconomicFaction()) {
            factionsComps.add(Component.text()
                    .append(Component.text("Economical faction:  "))
                    .append(economyFactionType1.getNameComponent())
                    .build());
        }
        MilitaryFactionType militaryFactionType1 = this.economy.getMilitaryFactionType();
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
            for (Country country : this.diplomacy.getPuppets()) {
                if (2 < i) {
                    showMore = true;
                    extraInfo.removeLast();
                    extraInfo.add(Component.text()
                            .append(Component.text(" [CLICK]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to show the list of all the puppets", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country info puppets " + getName()))
                            .build());
                    break;
                }
                extraInfo.add(country.info.getOriginalName());
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
                    .append(this.info.getOverlord().info.getOriginalName())
                    .build());
        }

        if (null == info.getLeader()) return;
        this.info.setDescription(Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(this.info.getOriginalName())
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(this.info.getLeader().getName())
                .clickEvent(ClickEvent.runCommand("/country leader " + getName()))
                .appendNewline()
                .append(Component.text("Modifiers: "))
                .append(modifierComps)
                .appendNewline()
                .append(leaderComp)
                .append(factionsComps)
                .appendNewline()
                .append(Component.text("Ideology: "))
                .append(this.info.getIdeology().getCurrentIdeology().getModifier().getName())
                .appendNewline()
                .append(extraInfo)
                .build());
    }

    public void addPuppet(Country country) {
        Country other = this;
        other.diplomacy.addDiplomaticRelation(country.getName(), 5);
        if (other.hasOverlord()) {
            final Country overlord = country.info.getOverlord();
            country.setOverlord(overlord);
            other = overlord;
        }
        if (!other.hasPuppets()) {
            country.info.setPuppetChat(new PuppetChat(other));
        }
        other.createInfo();
        PuppetChat puppetChat = country.info.getPuppetChat();
        country.info.getPlayers().forEach(puppetChat::addPlayer);
        country.diplomacy.addPuppet(country);
        Country finalOther = other;
        country.military.getCountryWars().forEach(country1 -> {
            Country country2 = ContinentalManagers.world(finalOther.instance).countryDataManager().getCountryFromName(country1);
            country.addCountryWar(country2);
            country2.addCountryWar(country);
        });
        country.info.getClientsides().forEach(other::addClientside);
    }

    public void removePuppet(Country country) {
        country.diplomacy.removePuppet(country);
        country.info.getClientsides().forEach(clientside -> this.info.getPlayers().forEach(clientside::removeViewer));
        if (hasPuppets()) {
            this.info.setPuppetChat(null);
        }
        createInfo();
    }

    public void setOverlord(Country country) {
        this.info.setOverlord(country);
        this.diplomacy.addDiplomaticRelation(country.getName(),5);
        country.getDiplomacy().getPuppets().forEach(country1 -> {
            if (country1==country)return;
            diplomacy.addDiplomaticRelation(country1.getName(),5);
        });
    }

    public void endGame() {
        this.info.getClientsides().forEach(Clientside::dispose);
        ContinentalManagers.centralAIManager.getAIManagerFor(instance).removeAi(this);
    }

    public boolean canJoinFaction(Faction faction) {
        return ((faction instanceof MilitaryFactionType) && (null == economy.getMilitaryFactionType())) || ((faction instanceof EconomyFactionType) && (null == economy.getEconomyFactionType()));
    }

    public boolean isLeaderOfAFaction() {
        return isEconomyFactionLeader() || isMilitaryFactionLeader();
    }

    public boolean isEconomyFactionLeader() {
        return null != economy.getEconomyFactionType() && this.economy.getEconomyFactionType().getLeader() == this;
    }

    public boolean isMilitaryFactionLeader() {
        return null != economy.getMilitaryFactionType() && this.economy.getMilitaryFactionType().getLeader() == this;
    }

    public boolean isMilitaryAlly(Country country) {
        return 6 == diplomacy.getDiplomaticRelation(country.getName());
    }

    public boolean isEconomicAlly(Country country) {
        return 4 == diplomacy.getDiplomaticRelation(country.getName());
    }

    public boolean isAlly(Country country) {
        int c = this.diplomacy.getDiplomaticRelation(country.getName());
        return 2 == c || 6 == c;
    }

    public boolean isInAFaction() {
        return isInAMilitaryFaction() || isInAnEconomicFaction();
    }

    public boolean isInAMilitaryFaction() {
        return null != economy.getMilitaryFactionType();
    }

    public boolean isInAnEconomicFaction() {
        return null != economy.getEconomyFactionType();
    }

    public boolean isInAllFactions() {
        return !isInAMilitaryFaction() && !isInAnEconomicFaction();
    }

    public void addBuilding(Building building) {
        this.info.addClientside(building.getItemDisplay());
        building.getItemDisplay().addCountry(this);
        this.economy.addBuilding(building);
    }

    public void removeBuilding(Building building) {
        this.info.removeClientside(building.getItemDisplay());
        building.getItemDisplay().removeCountry(this);
        this.economy.removeBuilding(building);
    }

    public boolean isPlayerLeader(Player player) {
        return this.info.getPlayerLeader() == player;
    }

    public void removeDemand(Demand demand, String name) {
        this.diplomacy.removeDemand(name);
        sendMessage(Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("The demand from "))
                .append(demand.getFromCountry().info.getOriginalName())
                .append(Component.text(" has been cancelled"))
                .build());
    }

    public void setPlayerLeader(CPlayer player) {
        if (null != info.getPlayerLeader()) this.info.getPlayerLeader().refreshCommands();
        this.info.setPlayerLeader(player);
    }

    public void nextWeek(NewDay newDay) {
        this.economy.getVault().calculateIncrease();
        if (null != research){
            this.research.researchVault().extraCalcIncrease();
            this.research.researchCountry().newWeek(newDay);
        }
        this.info.getStability().newWeek();
        newWeek(newDay);
    }

    protected abstract void newWeek(NewDay newDay);

    public void nextDay(NewDay newDay) {
        List<EventsRunner> e = new ArrayList<>();
        new ArrayList<>(this.economy.getEventsRunners()).forEach(eventsRunner -> {
            if (eventsRunner.newDay()) e.add(eventsRunner);
        });

        this.economy.removeEventsRunners(e);
        newDay(newDay);
    }

    public abstract void newDay(NewDay newDay);

    public void addNonAggressionPact(NonAggressionPact nonAggressionPact, Country other) {
        this.diplomacy.addNonAggressionPact(other.getName(),nonAggressionPact);
        loadCountriesDiplomacy(other);
    }

    public void removeNonAggressionPact(Country other) {
        this.diplomacy.removeNonAggressionPact(other.getName());
        loadCountriesDiplomacy(other);
    }

    public boolean canFight(Country country) {
        return !cantStartAWarWith(country);
    }

    public boolean canJustifyAgainst(Country country){
        return canFight(country)&&getDiplomacy().getCompletedWarJustification(country.getName())==null&&getDiplomacy().getWarJustification(country.getName())==null
                &&!isAtWar(country.getName())&&!getDiplomacy().hasCondition(ConditionEnum.cant_start_a_war)&&(!country.getInfo().isCapitulated()||getInfo().isCapitulated());
    }

    public int getDiplomaticRelations(String country){
        return this.diplomacy.getDiplomaticRelation(country);
    }

    public boolean isMilitaryFriend(Country country) {
        return 3 < country.getDiplomaticRelations(country.getName())||country==this;
    }

    public boolean cantStartAWarWith(Country country) {
        return 2 < country.getDiplomaticRelations(country.getName())||country==this;
    }

    public void addOthersCores(String country, Province province) {
        this.military.addOthersCoreProvince(country,province);
    }

    public void removeOthersCores(String country, Province province) {
        this.military.removeOthersCoreProvince(country,province);
    }

    public Ideology getIdeology(){
        return this.info.getIdeology();
    }

    public void puppet(Country overlord) {
        if (hasOverlord()) {
            this.info.getOverlord().removePuppet(this);
        } else {
            createInfo();
        }
        setOverlord(overlord);
        this.info.setBlock(overlord.info.getBlock());
        this.info.setBorder(overlord.info.getBorder());
        this.military.getOccupies().forEach(province -> {
            if (province.isBorder()) {
                province.setBorder();
            } else if (!province.isCity()) {
                province.setBlock();
            }
        });
    }

    public void addBorder(Province province, String country) {
        this.military.addBorder(country,province);
    }

    public void removeBorder(Province province, String country) {
        this.military.removeBorder(country,province);
    }

    public void loadCountriesDiplomacy(Country country){
        String name = country.getName();
        if (hasPuppets()) {
            if (diplomacy.containsPuppet(country)) {
                diplomacy.addDiplomaticRelation(name, 5);
                return;
            }
        }
        if (hasOverlord()) {
            if (Objects.equals(name, getInfo().getOverlord().getName())) {
                this.diplomacy.addDiplomaticRelation(name, 5);
            }
        }
        if (isMilitaryAlly(country)){
            this.diplomacy.addDiplomaticRelation(name,6);
            return;
        }
        if (isEconomicAlly(country)){
            this.diplomacy.addDiplomaticRelation(name,3);
            return;
        }
        if (this.diplomacy.containsNonAggressionPact(name)){
            this.diplomacy.addDiplomaticRelation(name,2);
            return;
        }
        if (getMilitary().containsCountryWar(name)){
            this.diplomacy.addDiplomaticRelation(name,0);
        }
        this.diplomacy.addDiplomaticRelation(name,1);
    }

    public JsonElement getReference(){
        return new JsonPrimitive(getName());
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.add("diplomacy",diplomacy.toJson());
        json.add("economy",economy.toJson());
        json.add("info",info.toJson());
        json.add("military",military.toJson());
        json.add("abstract",abstractToJson());
        return json;
    }

    public static Country fromJson(JsonObject jsonObject, Instance instance){
        String name = jsonObject.getAsString();

        JsonObject diplomacy = jsonObject.getAsJsonObject("diplomacy");
        JsonObject economy = jsonObject.getAsJsonObject("economy");
        JsonObject info = jsonObject.getAsJsonObject("info");
        JsonObject military = jsonObject.getAsJsonObject("military");
        JsonObject abstractJson = jsonObject.getAsJsonObject("abstract");

        HashMap<String,Integer> diplomacies = new HashMap<>();
        JsonObject diplomacys = diplomacy.getAsJsonObject("diplomacy");
        for (Map.Entry<String, JsonElement> entry : diplomacys.entrySet()) {
            String countryName = entry.getKey();
            int number = entry.getValue().getAsInt();
            diplomacies.put(countryName,number);
        }

        HashMap<String, NonAggressionPact> nonAggressionPactHashMap = new HashMap<>();
        JsonObject nonAggressionPactJsonObject = diplomacy.getAsJsonObject("nonAggressionPactHashMap");
//        for (Map.Entry<String, JsonElement> entry : nonAggressionPactJsonObject.entrySet()) {
//            nonAggressionPactHashMap.put(entry.getKey(),NonAggressionPact.fromJson(entry.getValue().getAsJsonObject(),instance));
//        }

        HashMap<String, WarJustification> warJustificationHashMap = new HashMap<>();
        JsonObject warJustificationObject = diplomacy.getAsJsonObject("warJustificationHashMap");
//        for (Map.Entry<String, JsonElement> entry : warJustificationObject.entrySet()) {
//            warJustificationHashMap.put(entry.getKey(),WarJustification.fromJson(entry.getValue().getAsJsonObject(),instance));
//        }
//
//        Diplomacy diplomacy1 = new Diplomacy(new ArrayList<>(),diplomacies,nonAggressionPactHashMap,warJustificationHashMap,);

        return null;
    }

    public Block getBlockForProvince(Province province) {
        return province.getMaterial().block();
    }

    protected abstract JsonElement abstractToJson();
}
