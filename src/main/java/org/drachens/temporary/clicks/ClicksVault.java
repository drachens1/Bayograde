package org.drachens.temporary.clicks;

import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.temporary.research.ResearchVault;

import java.util.HashMap;

public class ClicksVault extends ResearchVault {

    public ClicksVault(HashMap<CurrencyTypes, Currencies> startingCurrencies) {
        super(startingCurrencies);
    }
}
