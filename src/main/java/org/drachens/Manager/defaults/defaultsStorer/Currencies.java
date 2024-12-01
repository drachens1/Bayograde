package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.Manager.defaults.defaultsStorer.enums.CurrencyEnum;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Currencies {
    private final HashMap<String, CurrencyTypes> currencies = new HashMap<>();
    private final List<String> names = new ArrayList<>();

    public void register(CurrencyTypes currencyTypes) {
        currencies.put(currencyTypes.getIdentifier(), currencyTypes);
        if (names.contains(currencyTypes.getIdentifier())) return;
        names.add(currencyTypes.getIdentifier());
    }

    public void unregister(CurrencyTypes currencyTypes) {
        currencies.remove(currencyTypes.getIdentifier());
        names.remove(currencyTypes.getIdentifier());
    }

    public CurrencyTypes getCurrencyType(String name) {
        return currencies.get(name);
    }

    public CurrencyTypes getCurrencyType(CurrencyEnum currencyEnum){
        return currencies.get(currencyEnum.name());
    }

    public List<String> getCurrencyNames() {
        return names;
    }
}
