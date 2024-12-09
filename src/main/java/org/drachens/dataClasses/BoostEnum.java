package org.drachens.dataClasses;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static org.drachens.util.KyoriUtil.compBuild;

public enum BoostEnum {//todo make the symbols match up
    planes(compBuild("plane",NamedTextColor.BLUE)),
    troops(compBuild("troops",NamedTextColor.BLUE)),
    stabilityGain(compBuild("gain",NamedTextColor.BLUE)),
    stabilityBase(compBuild("base",NamedTextColor.BLUE)),
    relations(compBuild("relations",NamedTextColor.BLUE)),
    buildingSlotBoost(compBuild("\\uD83D\\uDC30", NamedTextColor.WHITE)),
    capitulation(compBuild("\\uD83D\\uDC3B", NamedTextColor.WHITE)),
    production(compBuild("production",NamedTextColor.BLUE)),
    gunCost(compBuild("gun_cost",NamedTextColor.BLUE)),
    gunAccuracy(compBuild("gun_acc",NamedTextColor.BLUE));

    private final Component symbol;
    private BoostEnum(Component component){
        this.symbol=component;
    }
    public Component getSymbol(){
        return symbol;
    }
}