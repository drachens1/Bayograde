package org.drachens.dataClasses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum BoostEnum {//todo make the symbols match up
    planes(Component.text("plane",NamedTextColor.BLUE),true),
    troops(Component.text("troops",NamedTextColor.BLUE),true),
    stabilityGain(Component.text("gain",NamedTextColor.BLUE),false),
    stabilityBase(Component.text("base",NamedTextColor.BLUE),false),
    relations(Component.text("relations",NamedTextColor.BLUE),true),
    buildingSlotBoost(Component.text("\\uD83D\\uDC30", NamedTextColor.WHITE),true),
    capitulation(Component.text("\\uD83D\\uDC3B", NamedTextColor.WHITE),true),
    production(Component.text("production",NamedTextColor.BLUE),true),
    gunCost(Component.text("gun_cost",NamedTextColor.BLUE),true),
    gunAccuracy(Component.text("gun_acc",NamedTextColor.BLUE),true);

    private final Component symbol;
    private final boolean percentage;
    private BoostEnum(Component component, boolean percentage){
        this.symbol=component;
        this.percentage=percentage;
    }
    public Component getSymbol(){
        return symbol;
    }
    public boolean isPercentage(){
        return percentage;
    }
}