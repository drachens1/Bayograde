package org.drachens.dataClasses.Economics.currency;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import static org.drachens.util.KyoriUtil.compBuild;

public class CurrencyTypes {
    private final String identifier;
    private final Component name;
    private final Component symbol;

    public CurrencyTypes(String identifier, NamedTextColor nameColour, String symbol, NamedTextColor symbolColour) {
        this.name = compBuild(identifier, nameColour);
        this.symbol = compBuild(symbol, symbolColour);
        this.identifier = identifier;
    }

    public CurrencyTypes(Component name, Component symbol, String identifier) {
        this.name = name;
        this.symbol = symbol;
        this.identifier = identifier;
    }

    public Component getName() {
        return name;
    }

    public Component getSymbol() {
        return symbol;
    }

    public String getIdentifier() {
        return identifier;
    }
}
