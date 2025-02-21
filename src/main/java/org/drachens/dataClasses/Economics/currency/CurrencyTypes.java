package org.drachens.dataClasses.Economics.currency;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

@Getter
public class CurrencyTypes {
    private final String identifier;
    private final Component name;
    private final Component symbol;

    public CurrencyTypes(String identifier, NamedTextColor nameColour, String symbol, NamedTextColor symbolColour) {
        this.name = Component.text(identifier, nameColour);
        this.symbol = Component.text(symbol, symbolColour);
        this.identifier = identifier;
    }

    public CurrencyTypes(Component name, Component symbol, String identifier) {
        this.name = name;
        this.symbol = symbol;
        this.identifier = identifier;
    }

}
