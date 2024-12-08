package org.drachens.Manager.defaults.defaultsStorer.enums;

import static org.drachens.util.KyoriUtil.compBuild;

import org.drachens.dataClasses.Economics.currency.CurrencyTypes;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

public enum CurrencyEnum {
    production(new CurrencyTypes(compBuild("production", TextColor.color(255, 165, 0)), compBuild("\uD83D\uDC35", TextColor.color(255, 165, 0)), "production");
),
    research(new CurrencyTypes(compBuild("Research",NamedTextColor.BLUE),compBuild("\uD83D\uDC41",NamedTextColor.WHITE),"research");
);

    private final CurrencyTypes currencyTypes;
    private CurrencyEnum(CurrencyTypes currencyTypes){
        this.currencyTypes=currencyTypes;
    }
    public CurrencyTypes getCurrencyType(){
        return currencyTypes;
    }
}
