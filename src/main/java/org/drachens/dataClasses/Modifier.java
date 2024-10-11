package org.drachens.dataClasses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.drachens.dataClasses.Economics.currency.CurrencyBoost;

import java.util.ArrayList;
import java.util.List;

public class Modifier {
    private final float maxBoost;
    private final float capitulationBoostPercentage;
    private final float stabilityBaseBoost;
    private final float stabilityGainBoost;
    private final List<CurrencyBoost> currencyBoostList;
    private final Component name;
    private final Component justCompName;
    private Component description;
    private Component startDescription;
    private final TextColor textColor;
    private Modifier(create c){
        this.justCompName = c.name;
        this.maxBoost = c.maxBoost;
        this.capitulationBoostPercentage = c.capitulationBoostPercentage;
        this.stabilityBaseBoost = c.stabilityBaseBoost;
        this.stabilityGainBoost = c.stabilityGainBoost;
        this.currencyBoostList = c.currencyBoostList;
        if (c.description!=null) this.startDescription = c.description;
        this.textColor = c.textColor;
        createDescription();
        this.name = Component.text()
                .append(c.name)
                .hoverEvent(HoverEvent.showText(description))
                .build();
    }

    public void createDescription(){
        List<Component> boostComp = new ArrayList<>();
        for (CurrencyBoost currencyBoost : currencyBoostList){
            if (currencyBoost.boost()>0){
                boostComp.add(Component.text()
                        .append(Component.text(currencyBoost.boost()*100, NamedTextColor.GREEN))
                        .append(Component.text("%", NamedTextColor.GREEN))
                        .append(currencyBoost.currencyTypes().getSymbol())
                        .appendNewline()
                        .build());
            }else {
                boostComp.add(Component.text()
                        .append(Component.text(currencyBoost.boost()*100, NamedTextColor.RED))
                        .append(Component.text("%", NamedTextColor.RED))
                        .append(currencyBoost.currencyTypes().getSymbol())
                        .appendNewline()
                        .build());
            }
        }
        if (maxBoost!=0f){
            if (maxBoost>0f){
                boostComp.add(Component.text()
                        .append(Component.text(maxBoost,NamedTextColor.GREEN))
                        .append(Component.text("Insert factory max symbol"))
                        .appendNewline()
                        .build());
            }else {
                boostComp.add(Component.text()
                        .append(Component.text(maxBoost,NamedTextColor.RED))
                        .append(Component.text("Insert factory max symbol"))
                        .appendNewline()
                        .build());
            }
        }
        if (stabilityBaseBoost!=0f){
            if (stabilityBaseBoost > 0f){
                boostComp.add(Component.text()
                        .append(Component.text(stabilityBaseBoost,NamedTextColor.GREEN))
                        .append(Component.text("insert stability symbol"))
                        .appendNewline()
                        .build());
            }else {
                boostComp.add(Component.text()
                        .append(Component.text(stabilityBaseBoost,NamedTextColor.RED))
                        .append(Component.text("insert stability symbol"))
                        .appendNewline()
                        .build());
            }
        }
        if (stabilityGainBoost!=0f){
            if (stabilityGainBoost > 0f){
                boostComp.add(Component.text()
                        .append(Component.text(stabilityBaseBoost,NamedTextColor.GREEN))
                        .append(Component.text("insert stability gain symbol"))
                        .appendNewline()
                        .build());
            }else {
                boostComp.add(Component.text()
                        .append(Component.text(stabilityBaseBoost,NamedTextColor.RED))
                        .append(Component.text("insert stability gain symbol"))
                        .appendNewline()
                        .build());
            }
        }
        if (capitulationBoostPercentage!=0f){
            if (capitulationBoostPercentage > 0f){
                boostComp.add(Component.text()
                        .append(Component.text(capitulationBoostPercentage,NamedTextColor.GREEN))
                        .append(Component.text("insert capitulation symbol"))
                        .appendNewline()
                        .build());
            }else {
                boostComp.add(Component.text()
                        .append(Component.text(capitulationBoostPercentage,NamedTextColor.RED))
                        .append(Component.text("insert capitulation symbol"))
                        .appendNewline()
                        .build());
            }
        }

        if (startDescription == null){
            description = Component.text()
                    .append(Component.text("_______/",NamedTextColor.BLUE))
                    .append(justCompName)
                    .append(Component.text("\\_______",NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Boosts: "))
                    .append(boostComp)
                    .build();
        }else {
            description = Component.text()
                    .append(Component.text("_______/",NamedTextColor.BLUE))
                    .append(justCompName)
                    .append(Component.text("\\_______",NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Boosts: "))
                    .append(boostComp)
                    .appendNewline()
                    .append(startDescription)
                    .build();
        }

    }

    public float getMaxBoost(){
        return maxBoost;
    }
    public float getCapitulationBoostPercentage(){
        return capitulationBoostPercentage;
    }
    public TextColor getTextColor(){
        return textColor;
    }
    public Component getName() {
        return name;
    }

    public Component getDescription() {
        return description;
    }

    public float getStabilityBaseBoost() {
        return stabilityBaseBoost;
    }

    public float getStabilityGainBoost() {
        return stabilityGainBoost;
    }

    public List<CurrencyBoost> getCurrencyBoostList() {
        return currencyBoostList;
    }

    public static class create{
        private final Component name;
        private Component description;
        private float maxBoost = 0f;
        private float capitulationBoostPercentage = 0f;
        private float stabilityBaseBoost = 0f;
        private float stabilityGainBoost = 0f;
        private final List<CurrencyBoost> currencyBoostList = new ArrayList<>();
        private TextColor textColor;
        public create(Component name){
            this.name = name;
        }
        public create setDescription(Component description){
            this.description = description;
            return this;
        }
        public create addCurrencyBoost(CurrencyBoost currencyBoost){
            currencyBoostList.add(currencyBoost);
            return this;
        }
        public create addMaxBoost(float amount){
            maxBoost+=amount;
            return this;
        }
        public create addCapitulationBoostPercentage(float amount){
            capitulationBoostPercentage+=amount;
            return this;
        }
        public create addStabilityBaseBoost(float amount){
            stabilityBaseBoost+=amount;
            return this;
        }
        public create addStabilityGainBoost(float amount){
            this.stabilityGainBoost+=amount;
            return this;
        }
        public create setTextColour(TextColor textColor){
            this.textColor = textColor;
            return this;
        }
        public create setTextColour(int r, int g, int b){
            this.textColor = TextColor.color(r,g,b);
            return this;
        }
        public Modifier build(){
            return new Modifier(this);
        }
    }
}
