package org.drachens.temporary.clicks;

import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.temporary.research.ResearchVault;

import java.util.Map;

public class ClicksVault extends ResearchVault {

    public ClicksVault(Map<CurrencyTypes, Currencies> startingCurrencies) {
        super(startingCurrencies);
    }
}
