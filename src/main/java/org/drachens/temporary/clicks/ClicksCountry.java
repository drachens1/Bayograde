package org.drachens.temporary.clicks;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Countries.CountryEnums;
import org.drachens.dataClasses.Countries.Election;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.laws.LawCategory;
import org.drachens.events.NewDay;
import org.drachens.temporary.research.ResearchCountry;

import java.util.HashMap;

public class ClicksCountry extends ResearchCountry {
    private CountryEnums.Type type;
    private CountryEnums.RelationsStyle relationsStyle;
    private CountryEnums.History history;
    private CountryEnums.Focuses focuses;
    private CountryEnums.PreviousWar previousWar;

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
    public void newDay(NewDay newDay) {

    }

    public CountryEnums.Type getType() {
        return type;
    }

    public void setType(CountryEnums.Type newType) {
        type = newType;
    }

    public CountryEnums.History getHistory() {
        return history;
    }

    public void setHistory(CountryEnums.History history) {
        this.history = history;
    }

    public CountryEnums.Focuses getFocuses() {
        return focuses;
    }

    public void setFocuses(CountryEnums.Focuses f) {
        this.focuses = f;
    }

    public CountryEnums.PreviousWar getPreviousWar() {
        return previousWar;
    }

    public void setPreviousWar(CountryEnums.PreviousWar p) {
        this.previousWar = p;
    }

    public CountryEnums.RelationsStyle getRelationsStyle() {
        return relationsStyle;
    }

    public void setRelationsStyle(CountryEnums.RelationsStyle relationsStyle) {
        this.relationsStyle = relationsStyle;
    }
}
