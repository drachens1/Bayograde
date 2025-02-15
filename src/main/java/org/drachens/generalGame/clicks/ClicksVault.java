package org.drachens.generalGame.clicks;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Vault;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;

import java.util.HashMap;
import java.util.List;

public class ClicksVault extends Vault {
    public ClicksVault(HashMap<CurrencyTypes, Currencies> startingCurrencies) {
        super(startingCurrencies);
    }

    @Override
    public void onCountrySet(Country country) {

    }

    @Override
    public void extraCalcIncrease() {

    }

    @Override
    protected List<Currencies> getCustomCurrencies() {
        return List.of();
    }
}
