package org.drachens.dataClasses;

import static org.drachens.util.KyoriUtil.compBuild;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public enum BoostEnum {//todo make the symbols match up
    planes(),
    troops(),
    stabilityGain(),
    stabilityBase(),
    relations(),
    buildingSlotBoost(compBuild("\\uD83D\\uDC30", NamedTextColor.WHITE))
    capitulation(compBuild("\\uD83D\\uDC3B", NamedTextColor.WHITE)),
    production(),
    gunCost(),
    gunAccuracy();

    private final Component symbol;
    private BoostEnum(Component component){
        this.symbol=component;
    }
    public Component getSymbol(){
        return symbol;
    }
}