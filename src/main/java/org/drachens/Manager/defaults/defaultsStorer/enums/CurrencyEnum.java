package org.drachens.Manager.defaults.defaultsStorer.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;

public enum CurrencyEnum {
    production(new CurrencyTypes(Component.text("production", TextColor.color(255, 165, 0)), Component.text("\uD83D\uDC35", TextColor.color(255, 165, 0)), "production")),
    research(new CurrencyTypes(Component.text("Research",NamedTextColor.BLUE),Component.text("\uD83D\uDC41",NamedTextColor.WHITE),"research"));

    private final CurrencyTypes currencyTypes;
    CurrencyEnum(CurrencyTypes currencyTypes){
        this.currencyTypes=currencyTypes;
    }
    public CurrencyTypes getCurrencyType(){
        return currencyTypes;
    }
}
