package org.drachens.dataClasses.Countries.countryClass;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.Loan;
import org.drachens.dataClasses.Economics.Vault;
import org.drachens.dataClasses.additional.BoostEnum;
import org.drachens.dataClasses.additional.EventsRunner;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.additional.ModifierCommand;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.dataClasses.other.CompletionBarTextDisplay;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.drachens.util.OtherUtil.bound;

@Getter
@Setter
public class Economy implements Saveable {
    private final Vault vault;
    private EconomyFactionType economyFactionType;
    private MilitaryFactionType militaryFactionType;
    private final HashMap<String, Loan> loanRequests = new HashMap<>();
    private final HashMap<BoostEnum, Float> boostHashmap = new HashMap<>();
    private final HashMap<String, Modifier> modifiers = new HashMap<>();
    private final HashMap<String, ModifierCommand> modifierCommandsHashMap = new HashMap<>();
    private final List<Modifier> visibleModifiers = new ArrayList<>();
    private final HashMap<String, LawCategory> laws = new HashMap<>();
    private final List<EventsRunner> eventsRunners = new ArrayList<>();
    private final HashMap<BuildingEnum, List<Building>> buildTypesListHashMap = new HashMap<>();
    private CompletionBarTextDisplay capitulationTextBar;

    public Economy(Vault vault, EconomyFactionType economyFactionType, MilitaryFactionType militaryFactionType,
                   HashMap<String, Loan> loanRequests, HashMap<BoostEnum, Float> boostHashmap,
                   CompletionBarTextDisplay capitulationTextBar, HashMap<String, Modifier> modifiers,
                   HashMap<String, ModifierCommand> modifierCommandsHashMap, List<Modifier> visibleModifiers,
                   HashMap<String, LawCategory> laws, List<EventsRunner> eventsRunners) {
        this.capitulationTextBar = capitulationTextBar;
        this.vault = vault;
        this.economyFactionType = economyFactionType;
        this.militaryFactionType = militaryFactionType;
        this.loanRequests.putAll(loanRequests);
        this.boostHashmap.putAll(boostHashmap);
        this.modifiers.putAll(modifiers);
        this.modifierCommandsHashMap.putAll(modifierCommandsHashMap);
        this.visibleModifiers.addAll(visibleModifiers);
        this.laws.putAll(laws);
        this.eventsRunners.addAll(eventsRunners);
    }

    public void addLoanRequest(String key, Loan loan) {
        loanRequests.put(key, loan);
    }

    public void removeLoanRequest(String key) {
        loanRequests.remove(key);
    }

    public boolean containsLoanRequest(String key) {
        return loanRequests.containsKey(key);
    }

    public Loan getLoan(String key){
        return loanRequests.get(key);
    }

    public float getBoost(BoostEnum boostType) {
        if (boostType.isPercentage()) {
            return bound(20, 0, boostHashmap.getOrDefault(boostType, 1.0f));
        }
        return boostHashmap.getOrDefault(boostType, 0.0f);
    }

    public void addBoost(BoostEnum boostType, float value) {
        float current = 0f;
        if (boostType.isPercentage()) {
            current = boostHashmap.getOrDefault(boostType, 1.0f);
        }
        current += value;
        boostHashmap.put(boostType, current);
    }

    public void removeBoost(BoostEnum boostType, float value) {
        float current = 0f;
        if (boostType.isPercentage()) {
            current = boostHashmap.getOrDefault(boostType, 1.0f);
        }
        current -= value;
        boostHashmap.put(boostType, current);
    }

    public boolean containsBoost(BoostEnum boostType) {
        return boostHashmap.containsKey(boostType);
    }

    public boolean hasModifier(String key){
        return modifiers.containsKey(key);
    }

    public void addModifier(String key, Modifier modifier) {
        modifiers.put(key, modifier);
    }

    public void removeModifier(String key) {
        modifiers.remove(key);
    }

    public boolean containsModifier(String key) {
        return modifiers.containsKey(key);
    }

    public void addModifierCommand(String key, ModifierCommand command) {
        modifierCommandsHashMap.put(key, command);
    }

    public void removeModifierCommand(String key) {
        modifierCommandsHashMap.remove(key);
    }

    public boolean containsModifierCommand(String key) {
        return modifierCommandsHashMap.containsKey(key);
    }

    public void addVisibleModifier(Modifier modifier) {
        if (!visibleModifiers.contains(modifier)) {
            visibleModifiers.add(modifier);
        }
    }

    public void removeVisibleModifier(Modifier modifier) {
        visibleModifiers.remove(modifier);
    }

    public boolean containsVisibleModifier(Modifier modifier) {
        return visibleModifiers.contains(modifier);
    }

    public Set<String> getLawNames(){
        return laws.keySet();
    }

    public LawCategory getLaw(String key){
        return laws.get(key);
    }

    public void addLaw(String key, LawCategory lawCategory) {
        laws.put(key, lawCategory);
    }

    public void removeLaw(String key) {
        laws.remove(key);
    }

    public boolean containsLaw(String key) {
        return laws.containsKey(key);
    }

    public void addEventsRunner(EventsRunner eventsRunner){
        eventsRunners.add(eventsRunner);
    }

    public void removeEventsRunner(EventsRunner eventsRunner){
        eventsRunners.remove(eventsRunner);
    }

    public void removeEventsRunners(List<EventsRunner> eventsRunner){
        eventsRunners.removeAll(eventsRunner);
    }

    public void addBuilding(Building building) {
        buildTypesListHashMap.computeIfAbsent(building.getBuildTypes(), k -> new ArrayList<>()).add(building);
    }

    public void removeBuilding(Building building) {
        List<Building> buildings = buildTypesListHashMap.get(building.getBuildTypes());
        if (null != buildings) {
            buildings.remove(building);
        }
    }

    public void removeAllBuildingOfType(BuildingEnum type) {
        buildTypesListHashMap.remove(type);
    }

    public List<Building> getBuildingType(BuildingEnum type) {
        return buildTypesListHashMap.getOrDefault(type, new ArrayList<>());
    }

    public Modifier getModifier(String name){
        return modifiers.get(name);
    }

    public Set<String> getModifierNames(){
        return modifiers.keySet();
    }

    public ModifierCommand getModifierCommmand(String key){
        return modifierCommandsHashMap.get(key);
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.add("vault", vault.toJson());
        json.add("economyFactionType", gson.toJsonTree(economyFactionType));
        json.add("militaryFactionType", gson.toJsonTree(militaryFactionType));
        json.add("loanRequests", gson.toJsonTree(loanRequests));
        json.add("boostHashmap", gson.toJsonTree(boostHashmap));
        json.add("modifiers",gson.toJsonTree(modifiers));
        json.add("laws", gson.toJsonTree(laws));
        JsonArray events = new JsonArray();
        eventsRunners.forEach(eventsRunner -> events.add(eventsRunner.toJson()));
        json.add("eventsRunners", events);
        json.add("buildTypesListHashMap", gson.toJsonTree(buildTypesListHashMap));
        return json;
    }
}
