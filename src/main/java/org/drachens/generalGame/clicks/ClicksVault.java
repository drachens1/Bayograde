package org.drachens.generalGame.clicks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Vault;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.interfaces.Saveable;

import java.util.HashMap;
import java.util.List;

import static org.drachens.util.JsonUtil.saveHashMap;

public class ClicksVault extends Vault implements Saveable {
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

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        HashMap<String, Float> amountCopy = new HashMap<>();
        amount.forEach((currencyTypes, currencies) -> amountCopy.put(currencyTypes.getIdentifier(), currencies.getAmount()));
        saveHashMap(amountCopy,Float.class,jsonObject,"amount");
        return jsonObject;
    }
}
