package org.drachens.dataClasses.additional;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

@Getter
public class Modifier implements Saveable, Cloneable  {
    private final Component justCompName;
    private final List<Country> appliedCountries = new ArrayList<>();
    private final HashMap<BoostEnum, Float> boostHashMap;
    private final HashSet<ConditionEnum> conditionEnums;
    private final List<EventsRunner> eventsRunners;
    private final List<ModifierCommand> modifierCommands;
    private final String identifier;
    private Component name;
    private Component description;
    private Component startDescription;
    private Modifier oldModifier;
    private boolean display;

    protected Modifier(create c) {
        this.justCompName = c.name;
        this.boostHashMap = c.boostHashMap;
        this.eventsRunners = c.eventsRunners;
        this.modifierCommands = c.modifierCommands;
        this.identifier = c.identifier;
        if (null != c.description) this.startDescription = c.description;
        display = c.display;
        conditionEnums = c.conditionEnums;
        createDescription();
        oldModifier = this.clone();
    }

    protected Modifier(Modifier c) {
        this.justCompName = c.name;
        this.boostHashMap = new HashMap<>(c.boostHashMap);
        this.eventsRunners = new ArrayList<>(c.eventsRunners);
        this.modifierCommands = new ArrayList<>(c.modifierCommands);
        this.identifier = c.identifier;
        if (null != c.description) this.startDescription = c.description;
        display = c.display;
        conditionEnums = new HashSet<>(c.conditionEnums);
        createDescription();
        oldModifier = this.clone();
    }

    public boolean shouldDisplay() {
        return display;
    }

    public void createDescription() {
        if (!shouldDisplay() || null == this.justCompName) return;
        List<Component> boostComp = new ArrayList<>();
        if (!boostHashMap.isEmpty()) {
            boostComp.add(Component.text()
                    .append(Component.text("Boosts: "))
                    .appendNewline()
                    .build());
            for (Entry<BoostEnum, Float> e : boostHashMap.entrySet()) {
                float value = e.getValue();
                Component symbol;
                if (0 > e.getValue()) {
                    symbol = e.getKey().getNegSymbol();
                } else {
                    symbol = e.getKey().getPosSymbol();
                }
                if (e.getKey().isPercentage()) {
                    if (0 < value) {
                        boostComp.add(Component.text()
                                .append(Component.text("+" + Math.round(value * 100), NamedTextColor.GREEN))
                                .append(Component.text("%", NamedTextColor.GREEN))
                                .append(symbol)
                                .appendNewline().build());
                    } else {
                        boostComp.add(Component.text()
                                    .append(Component.text(Math.round(value * 100), NamedTextColor.RED))
                                    .append(Component.text("%", NamedTextColor.RED))
                                    .append(symbol)
                                    .appendNewline().build());
                    }
                } else if (0 < value) {
                    boostComp.add(Component.text()
                            .append(Component.text("+" + value, NamedTextColor.GREEN))
                            .append(symbol)
                            .appendNewline().build());
                } else {
                    boostComp.add(Component.text()
                                .append(Component.text(value, NamedTextColor.RED))
                                .append(symbol)
                                .appendNewline().build());
                }
            }
        }
        if (!conditionEnums.isEmpty()) {
            boostComp.add(Component.text()
                    .append(Component.text("Conditions: "))
                    .appendNewline().build());
            for (ConditionEnum conditionEnum : conditionEnums) {
                boostComp.add(Component.text()
                        .append(conditionEnum.getDescription())
                        .appendNewline().build());
            }
        }

        if (!modifierCommands.isEmpty()) {
            boostComp.add(Component.text()
                    .append(Component.text("Modifier Commands: "))
                    .appendNewline().build());
            for (ModifierCommand modifierCommand : modifierCommands) {
                boostComp.add(Component.text()
                        .append(Component.text(modifierCommand.getString()))
                        .appendNewline().build());
            }
        }

        if (null == this.startDescription) {
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
        c.boostHashMap.forEach(this::addBoost);
        update();
    }

    public void setBoost(BoostEnum boostEnum, float amount) {
        boostHashMap.put(boostEnum, amount);
        update();
    }

    public void addBoost(BoostEnum boostEnum, float amount) {
        float current = boostHashMap.getOrDefault(boostEnum, 0.0f);
        boostHashMap.put(boostEnum, current + amount);
        update();
    }

    public void addBoosts(HashMap<BoostEnum, Float> boosts) {
        boosts.forEach((boostEnum, aFloat) -> {
            float current = boostHashMap.getOrDefault(boostEnum, 0.0f);
            boostHashMap.put(boostEnum, current + aFloat);
        });
        update();
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

    public float getBoost(BoostEnum boostEnum) {
        return boostHashMap.getOrDefault(boostEnum, 0.0f);
    }

    public void setName(Component name) {
        this.name = name;
        update();
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
        for (Country country : new ArrayList<>(appliedCountries)) {
            country.updateModifier(this, oldModifier);
            country.createInfo();
        }
        oldModifier = this.clone();
    }

    public void setShouldDisplay(boolean b) {
        display = b;
        appliedCountries.forEach(Country::createInfo);
    }

    public Modifier independantClone() {
        return new Modifier(this);
    }

    @Override
    public Modifier clone() {
        try {
            return (Modifier) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public JsonElement toJson() {
        System.out.println("identifier");
        return new JsonPrimitive(identifier);
    }

    public JsonElement actualJson() {
        return new JsonPrimitive(identifier);
    }

    public static class create {
        private final Component name;
        private final List<ModifierCommand> modifierCommands = new ArrayList<>();
        private final HashSet<ConditionEnum> conditionEnums = new HashSet<>();
        private final HashMap<BoostEnum, Float> boostHashMap = new HashMap<>();
        private final List<EventsRunner> eventsRunners = new ArrayList<>();
        private Component description;
        private boolean display = true;
        private String identifier;

        public create(Component name, String identifier) {
            this.name = name;
            this.identifier = identifier;
        }

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

        public create addCondition(ConditionEnum conditionEnum) {
            conditionEnums.add(conditionEnum);
            return this;
        }

        public create addEventsRunner(EventsRunner eventsRunner) {
            eventsRunners.add(eventsRunner);
            return this;
        }

        public create setDisplay(boolean choice) {
            display = choice;
            return this;
        }

        public create addModifierCommand(ModifierCommand modifierCommand) {
            modifierCommands.add(modifierCommand);
            return this;
        }

        public Modifier build() {
            return new Modifier(this);
        }
    }
}
