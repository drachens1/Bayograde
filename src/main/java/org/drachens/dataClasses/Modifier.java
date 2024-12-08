package org.drachens.dataClasses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyBoost;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Modifier implements Cloneable {
    private final Component justCompName;
    private final List<Country> appliedCountries = new ArrayList<>();
    private Component name;
    private Component description;
    private Component startDescription;
    private TextColor textColor;
    private Modifier oldModifier;
    private final boolean display;
    private HashMap<BoostEnum, Float> boostHashMap;

    protected Modifier(create c) {
        this.justCompName = c.name;
        this.boostHashMap=c.boostHashMap;
        if (c.description != null) this.startDescription = c.description;
        this.textColor = c.textColor;
        createDescription();
        this.name = Component.text()
                .append(c.name)
                .hoverEvent(HoverEvent.showText(description))
                .build();
        oldModifier = this.clone();
        display=c.display;
    }

    public boolean shouldDisplay(){
        return display;
    }

    public void createDescription() {
        if (!shouldDisplay())return;
        List<Component> boostComp = new ArrayList<>();
        for (Entry<BoostEnum, Float> e : boostHashMap.entrySet()){
            float value = e.getValue();
            Component symbol = e.getKey().getSymbol();
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
        }


        if (startDescription == null) {
            description = Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(justCompName)
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Boosts: "))
                    .appendNewline()
                    .append(boostComp)
                    .build();
        } else {
            description = Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(justCompName)
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Boosts: "))
                    .appendNewline()
                    .append(boostComp)
                    .appendNewline()
                    .append(startDescription)
                    .build();
        }

    }

    public void addModifier(Modifier c){
        //todo update this to make it work
        update();
    }

    public float getBoost(BoostEnum boostEnum){
        return boostHashMap.get(boostEnum);
    }

    public void setBoost(BoostEnum boostEnum, float amount){
        boostHashMap.put(boostEnum,amount);
        update();
    }

    public TextColor getTextColor() {
        return textColor;
    }

    public void setTextColor(TextColor textColor) {
        this.textColor = textColor;
        update();
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
        for (Country country : appliedCountries) {
            country.updateModifier(this, oldModifier);
        }
        oldModifier = this.clone();
    }

    @Override
    public Modifier clone() {
        try {
            Modifier clone = (Modifier) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public static class create {
        private final Component name;
        private Component description;
        private HashMap<BoostEnum, Float> boostHashMap = new HashMap<>();
        private TextColor textColor;
        private boolean display = true;

        public create(Component name) {
            this.name = name;
        }

        public create setDescription(Component description) {
            this.description = description;
            return this;
        }

        public create addBoost(BoostEnum boostEnum, float amount){
            boostHashMap.put(boostEnum, amount);
            return this;
        }

        public create setTextColour(TextColor textColor) {
            this.textColor = textColor;
            return this;
        }

        public create setTextColour(int r, int g, int b) {
            this.textColor = TextColor.color(r, g, b);
            return this;
        }
        
        public create setDisplay(boolean choice){
            display=choice;
            return this;
        }

        public Modifier build() {
            return new Modifier(this);
        }
    }
}
