package org.drachens.dataClasses.additional;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.dataClasses.Countries.Country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

public class Modifier implements Cloneable {
    private final Component justCompName;
    private final List<Country> appliedCountries = new ArrayList<>();
    private Component name;
    private Component description;
    private Component startDescription;
    private Modifier oldModifier;
    private boolean display;
    private final HashMap<BoostEnum, Float> boostHashMap;
    private final HashSet<ConditionEnum> conditionEnums;
    private final List<EventsRunner> eventsRunners;

    protected Modifier(create c) {
        this.justCompName = c.name;
        this.boostHashMap = c.boostHashMap;
        this.eventsRunners = c.eventsRunners;
        if (c.description != null) this.startDescription = c.description;
        display = c.display;
        conditionEnums=c.conditionEnums;
        createDescription();
        oldModifier = this.clone();
    }

    public HashMap<BoostEnum, Float> getBoostHashMap() {
        return boostHashMap;
    }

    public boolean shouldDisplay() {
        return display;
    }

    public void createDescription() {
        if (!shouldDisplay() || justCompName == null) return;
        List<Component> boostComp = new ArrayList<>();
        if (!boostHashMap.isEmpty()){
            boostComp.add(Component.text()
                            .append(Component.text("Boosts: "))
                            .appendNewline()
                    .build());
            for (Entry<BoostEnum, Float> e : boostHashMap.entrySet()) {
                float value = e.getValue();
                Component symbol = e.getKey().getSymbol();
                if (e.getKey().isPercentage()) {
                    if (value > 0) {
                        boostComp.add(Component.text()
                                .append(Component.text("+" + Math.round(value * 100), NamedTextColor.GREEN))
                                .append(Component.text("%", NamedTextColor.GREEN))
                                .append(symbol)
                                .appendNewline()
                                .build());
                    } else {
                        boostComp.add(Component.text()
                                .append(Component.text(Math.round(value * 100), NamedTextColor.RED))
                                .append(Component.text("%", NamedTextColor.RED))
                                .append(symbol)
                                .appendNewline()
                                .build());
                    }
                } else {
                    if (value > 0) {
                        boostComp.add(Component.text()
                                .append(Component.text("+" + value, NamedTextColor.GREEN))
                                .append(symbol)
                                .appendNewline()
                                .build());
                    } else {
                        boostComp.add(Component.text()
                                .append(Component.text(value, NamedTextColor.RED))
                                .append(symbol)
                                .appendNewline()
                                .build());
                    }
                }

            }
        }
        if (!conditionEnums.isEmpty()){
            boostComp.add(Component.text()
                    .append(Component.text("Conditions: "))
                    .appendNewline()
                    .build());
            for (ConditionEnum conditionEnum : conditionEnums){
                boostComp.add(Component.text()
                        .append(conditionEnum.getDescription())
                        .appendNewline()
                        .build());
            }
        }

        if (startDescription == null) {
            description = Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(justCompName)
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(boostComp)
                    .build();
        } else {
            description = Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(justCompName)
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(boostComp)
                    .append(startDescription)
                    .build();
        }

        this.name = Component.text()
                .append(justCompName)
                .hoverEvent(HoverEvent.showText(description))
                .build();
    }

    public void addModifier(Modifier c) {
        c.getBoostHashMap().forEach(this::addBoost);
        update();
    }

    public void setBoost(BoostEnum boostEnum, float amount) {
        boostHashMap.put(boostEnum, amount);
        update();
    }

    public void addBoost(BoostEnum boostEnum, float amount) {
        float current = boostHashMap.getOrDefault(boostEnum, 1f);
        boostHashMap.put(boostEnum, current + amount);
    }

    public HashSet<ConditionEnum> getConditionEnums(){
        return conditionEnums;
    }

    public void addCondition(ConditionEnum conditionEnum){
        conditionEnums.add(conditionEnum);
    }

    public void removeCondition(ConditionEnum conditionEnum){
        conditionEnums.remove(conditionEnum);
    }

    public Component getName() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
        update();
    }

    public Component getDescription() {
        return description;
    }

    public void setDescription(Component description) {
        this.description = description;
        update();
    }

    public void addCountry(Country country) {
        this.appliedCountries.add(country);
    }

    public void removeCountry(Country country) {
        this.appliedCountries.remove(country);
    }

    public void update() {
        createDescription();
        for (Country country : appliedCountries) {
            country.updateModifier(this, oldModifier);
            country.createInfo();
        }
        oldModifier = this.clone();
    }

    public void setShouldDisplay(boolean b){
        display=b;
        appliedCountries.forEach(Country::createInfo);
    }

    @Override
    public Modifier clone() {
        try {
            return (Modifier) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class create {
        private final Component name;
        private Component description;
        private final HashSet<ConditionEnum> conditionEnums = new HashSet<>();
        private final HashMap<BoostEnum, Float> boostHashMap = new HashMap<>();
        private final List<EventsRunner> eventsRunners = new ArrayList<>();
        private boolean display = true;

        public create(Component name) {
            this.name = name;
        }

        public create setDescription(Component description) {
            this.description = description;
            return this;
        }

        public create addBoost(BoostEnum boostEnum, float amount) {
            boostHashMap.put(boostEnum, amount);
            return this;
        }

        public create addCondition(ConditionEnum conditionEnum){
            conditionEnums.add(conditionEnum);
            return this;
        }

        public create addEventsRunner(EventsRunner eventsRunner){
            eventsRunners.add(eventsRunner);
            return this;
        }

        public create setDisplay(boolean choice) {
            display = choice;
            return this;
        }

        public Modifier build() {
            return new Modifier(this);
        }
    }
}
