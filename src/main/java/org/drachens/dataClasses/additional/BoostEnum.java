package org.drachens.dataClasses.additional;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.function.Function;

public enum BoostEnum {//todo make the symbols match up
    justification(Component.text("Justification time",NamedTextColor.BLUE),true),
    ideologyGain(Component.text("Ideology gain",NamedTextColor.BLUE),true),
    recruitablePop(Component.text("recruitable population",NamedTextColor.BLUE),false),
    planes(Component.text("plane", NamedTextColor.BLUE), true),
    troops(Component.text("troops", NamedTextColor.BLUE), true),
    stabilityGain(Component.text("gain", NamedTextColor.BLUE), false),
    stabilityBase(Component.text("base", NamedTextColor.BLUE), false),
    relations(Component.text("relations", NamedTextColor.BLUE), true),
    buildingSlotBoost(Component.text("\uD83D\uDC30", NamedTextColor.WHITE), true),
    capitulation(Component.text("\uD83D\uDC3B", NamedTextColor.WHITE), true),
    production(Component.text("production", NamedTextColor.BLUE), true),
    gunCost(Component.text("gun_cost", NamedTextColor.BLUE), true),
    gunAccuracy(Component.text("gun_acc", NamedTextColor.BLUE), true);

    private Component symbol;
    private boolean percentage;
    private Function<Float, Component> c;

    BoostEnum(Component component, boolean percentage) {
        this.symbol = component;
        this.percentage = percentage;
    }

    BoostEnum(Function<Float, Component> c) {
        this.c=c;
    }

    public Component getSymbol() {
        return symbol;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public Function<Float, Component> getFunction(){
        return c;
    }
}