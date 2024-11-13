package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.dataClasses.Economics.currency.CurrencyTypes;

import java.util.HashMap;

public class Currencies {
    HashMap<String, CurrencyTypes> currencies = new HashMap<>();

    public void register(CurrencyTypes currencyTypes) {
        currencies.put(currencyTypes.getIdentifier(), currencyTypes);
    }

    public void unregister(CurrencyTypes currencyTypes) {
        currencies.remove(currencyTypes.getIdentifier());
    }

    public CurrencyTypes getCurrencyType(String name) {
        return currencies.get(name);
    }
}
