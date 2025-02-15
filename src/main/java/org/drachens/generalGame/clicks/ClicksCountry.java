package org.drachens.generalGame.clicks;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.kyori.adventure.text.Component;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.Election;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.events.NewDay;
import org.drachens.interfaces.Saveable;
import org.drachens.player_types.CPlayer;

import java.util.HashMap;

public class ClicksCountry extends Country implements Saveable {

    public ClicksCountry(HashMap<CurrencyTypes, Currencies> startingCurrencies, String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Election election, Instance instance, HashMap<String, LawCategory> laws) {
        super(name, nameComponent, block, border, defaultIdeologies, election, instance, new ClicksVault(startingCurrencies), laws);
    }

    @Override
    protected void onAddPlayer(CPlayer p) {

    }

    @Override
    protected void onRemovePlayer(CPlayer p) {

    }

    @Override
    protected void newWeek(NewDay newDay) {

    }

    @Override
    public void newDay(NewDay newDay) {

    }

    @Override
    protected JsonElement abstractToJson() {
        return new JsonObject();
    }
}
