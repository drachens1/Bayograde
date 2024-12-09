package org.drachens.dataClasses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum BoostEnum {//todo make the symbols match up
    planes(Component.text("plane",NamedTextColor.BLUE)),
    troops(Component.text("troops",NamedTextColor.BLUE)),
    stabilityGain(Component.text("gain",NamedTextColor.BLUE)),
    stabilityBase(Component.text("base",NamedTextColor.BLUE)),
    relations(Component.text("relations",NamedTextColor.BLUE)),
    buildingSlotBoost(Component.text("\\uD83D\\uDC30", NamedTextColor.WHITE)),
    capitulation(Component.text("\\uD83D\\uDC3B", NamedTextColor.WHITE)),
    production(Component.text("production",NamedTextColor.BLUE)),
    gunCost(Component.text("gun_cost",NamedTextColor.BLUE)),
    gunAccuracy(Component.text("gun_acc",NamedTextColor.BLUE));

    private final Component symbol;
    private BoostEnum(Component component){
        this.symbol=component;
    }
    public Component getSymbol(){
        return symbol;
    }
}