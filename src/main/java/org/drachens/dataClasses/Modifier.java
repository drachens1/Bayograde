package org.drachens.dataClasses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.currency.CurrencyBoost;

import java.util.ArrayList;
import java.util.List;

public class Modifier implements Cloneable {
    private final Component justCompName;
    private final List<Country> appliedCountries = new ArrayList<>();
    private float productionBoost;
    private float capitulationBoostPercentage;
    private float maxBuildingSlotBoost;
    private List<CurrencyBoost> currencyBoostList;
    private Component name;
    private Component description;
    private Component startDescription;
    private TextColor textColor;
    private float relationsBoost;
    private float baseRelationsBoost;
    private Modifier oldModifier;
    private final boolean display;

    protected Modifier(create c) {
        this.justCompName = c.name;
        this.capitulationBoostPercentage = c.capitulationBoostPercentage;
        this.currencyBoostList = c.currencyBoostList;
        this.relationsBoost = c.relationsBoost;
        this.baseRelationsBoost = c.baseRelationsBoost;
        this.maxBuildingSlotBoost = c.maxBuildingSlotBoost;
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
        List<Component> boostComp = new ArrayList<>();
        for (CurrencyBoost currencyBoost : currencyBoostList) {
            if (currencyBoost.getBoost() > 0) {
                boostComp.add(Component.text()
                        .append(Component.text("+" + Math.round(currencyBoost.getBoost() * 100), NamedTextColor.GREEN))
                        .append(Component.text("%", NamedTextColor.GREEN))
                        .append(currencyBoost.getCurrencyTypes().getSymbol())
                        .appendNewline()
                        .build());
            } else {
                boostComp.add(Component.text()
                        .append(Component.text(Math.round(currencyBoost.getBoost() * 100), NamedTextColor.RED))
                        .append(Component.text("%", NamedTextColor.RED))
                        .append(currencyBoost.getCurrencyTypes().getSymbol())
                        .appendNewline()
                        .build());
            }
        }
        if (capitulationBoostPercentage != 0f) {
            if (capitulationBoostPercentage > 0f) {
                boostComp.add(Component.text()
                        .append(Component.text("+" + Math.round(capitulationBoostPercentage * 100) + "%", NamedTextColor.GREEN))
                        .append(Component.text("\uD83D\uDC3B"))
                        .appendNewline()
                        .build());
            } else {
                boostComp.add(Component.text()
                        .append(Component.text(Math.round(capitulationBoostPercentage * 100) + "%", NamedTextColor.RED))
                        .append(Component.text("\uD83D\uDC3B"))
                        .appendNewline()
                        .build());
            }
        }
        if (relationsBoost != 0f) {
            if (relationsBoost > 0f) {
                boostComp.add(Component.text()
                        .append(Component.text("+" + Math.round(relationsBoost * 100) + "%", NamedTextColor.GREEN))
                        .append(Component.text("relations boost"))
                        .appendNewline()
                        .build());
            } else {
                boostComp.add(Component.text()
                        .append(Component.text(Math.round(relationsBoost * 100) + "%", NamedTextColor.RED))
                        .append(Component.text("relations boost"))
                        .appendNewline()
                        .build());
            }
        }
        if (maxBuildingSlotBoost != 0f) {
            if (maxBuildingSlotBoost > 0f) {
                boostComp.add(Component.text()
                        .append(Component.text("+" + Math.round(maxBuildingSlotBoost * 100) + "%", NamedTextColor.GREEN))
                        .append(Component.text("\uD83D\uDC30"))
                        .appendNewline()
                        .build());
            } else {
                boostComp.add(Component.text()
                        .append(Component.text(Math.round(maxBuildingSlotBoost * 100) + "%", NamedTextColor.RED))
                        .append(Component.text("\uD83E\uDD8A"))
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
        this.capitulationBoostPercentage = c.capitulationBoostPercentage;
        this.currencyBoostList = c.currencyBoostList;
        this.relationsBoost = c.relationsBoost;
        this.baseRelationsBoost = c.baseRelationsBoost;
        this.maxBuildingSlotBoost = c.maxBuildingSlotBoost;
        update();
    }

    public float getMaxBuildingSlotBoost() {
        return maxBuildingSlotBoost;
    }

    public float getCapitulationBoostPercentage() {
        return capitulationBoostPercentage;
    }

    public void setCapitulationBoostPercentage(float capitulationBoostPercentage) {
        this.capitulationBoostPercentage = capitulationBoostPercentage;
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

    public List<CurrencyBoost> getCurrencyBoostList() {
        return currencyBoostList;
    }

    public void setCurrencyBoostList(List<CurrencyBoost> currencyBoostList) {
        this.currencyBoostList = currencyBoostList;
        update();
    }

    public float getProductionBoost() {
        return productionBoost;
    }

    public void setProductionBoost(float productionBoost) {
        this.productionBoost = productionBoost;
        update();
    }

    public float getRelationsBoost() {
        return relationsBoost;
    }

    public void setRelationsBoost(float relationsBoost) {
        this.relationsBoost = relationsBoost;
        update();
    }

    public float getBaseRelationsBoost() {
        return baseRelationsBoost;
    }

    public void setBaseRelationsBoost(float baseRelationsBoost) {
        this.baseRelationsBoost = baseRelationsBoost;
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
        private final List<CurrencyBoost> currencyBoostList = new ArrayList<>();
        private float maxBuildingSlotBoost = 0f;
        private Component description;
        private float maxBoost = 0f;
        private float capitulationBoostPercentage = 0f;
        private float stabilityBaseBoost = 0f;
        private float stabilityGainBoost = 0f;
        private float relationsBoost = 0f;
        private float baseRelationsBoost = 0f;
        private float productionBoost = 0f;
        private TextColor textColor;
        private boolean display = true;

        public create(Component name) {
            this.name = name;
        }

        public create setDescription(Component description) {
            this.description = description;
            return this;
        }

        public create addCurrencyBoost(CurrencyBoost currencyBoost) {
            currencyBoostList.add(currencyBoost);
            return this;
        }

        public create addMaxBoost(float amount) {
            maxBoost += amount;
            return this;
        }

        public create addCapitulationBoostPercentage(float amount) {
            capitulationBoostPercentage += amount;
            return this;
        }

        public create addStabilityBaseBoost(float amount) {
            stabilityBaseBoost += amount;
            return this;
        }

        public create addStabilityGainBoost(float amount) {
            this.stabilityGainBoost += amount;
            return this;
        }

        public create setTextColour(TextColor textColor) {
            this.textColor = textColor;
            return this;
        }

        public create setRelationsBoost(float amount) {
            relationsBoost = amount;
            return this;
        }

        public create setBaseRelationsBoost(float amount) {
            baseRelationsBoost = amount;
            return this;
        }

        public create setTextColour(int r, int g, int b) {
            this.textColor = TextColor.color(r, g, b);
            return this;
        }

        public create setProductionBoost(float amount) {
            this.productionBoost = amount;
            return this;
        }

        public create setMaxBuildingSlotBoost(float maxBuildingSlotBoost) {
            this.maxBuildingSlotBoost = maxBuildingSlotBoost;
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
