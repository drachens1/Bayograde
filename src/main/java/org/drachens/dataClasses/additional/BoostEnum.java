package org.drachens.dataClasses.additional;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.function.Function;

public enum BoostEnum {
    justification("\uD83D\uDC31", "\uD83E\uDD9D", true),
    ideologyGain("\uD83D\uDC29", "\uD83D\uDC3A", true),
    recruitablePop("rec1", "rec2", false),
    planes("p1", "p2", true),
    troops("t1", "t2", true),
    stabilityGain("\uD83E\uDD8D", "\uD83E\uDDA7", false),
    stabilityBase("\uD83D\uDC36", "\uD83E\uDDAE", false),
    relations("r1", "r2", true),
    buildingSlotBoost("\uD83D\uDC30", "neg", true),
    capitulation("\uD83D\uDC3B", "\uD83D\uDC3B", true),
    production("\uD83D\uDC34", "\uD83D\uDC08", true),
    gunCost("Gun Cost", "Gun Cost", true),
    gunAccuracy("gunacc1", "gunacc2", true);

    private Component posSymbol;
    private Component negSymbol;
    private boolean percentage;
    private Function<Float, Component> c;

    BoostEnum(String posSymbol, String negSymbol, boolean percentage) {
        this.posSymbol = Component.text(posSymbol, NamedTextColor.WHITE);
        this.negSymbol = Component.text(negSymbol, NamedTextColor.WHITE);
        this.percentage = percentage;
    }

    BoostEnum(Function<Float, Component> c) {
        this.c = c;
    }

    public Component getPosSymbol() {
        return posSymbol;
    }

    public Component getNegSymbol() {
        return negSymbol;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public Function<Float, Component> getFunction() {
        return c;
    }
}