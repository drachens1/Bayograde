package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static org.drachens.util.KyoriUtil.compBuild;

public class CurrencyTypes {
    private final Component name;
    private final Component symbol;

    public CurrencyTypes(String name, NamedTextColor nameColour, String symbol, NamedTextColor symbolColour) {
        this.name = compBuild(name, nameColour);
        this.symbol = compBuild(symbol, symbolColour);
    }

    public CurrencyTypes(Component name, Component symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public Component getName() {
        return name;
    }

    public Component getSymbol() {
        return symbol;
    }
}
