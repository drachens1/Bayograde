package org.drachens.events.countries.war;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.interfaces.Event;

public class StartWarEvent extends Event {
    private final Country aggressor;
    private final Country defender;
    private final WarJustification warJustification;

    public StartWarEvent(Country aggressor, Country occupier, WarJustification warJustification) {
        super(aggressor.getInstance());
        this.aggressor = aggressor;
        this.defender = occupier;
        this.warJustification=warJustification;
    }

    public Country getAggressor() {
        return aggressor;
    }

    public Country getDefender() {
        return defender;
    }

    public WarJustification getWarJustification() {
        return warJustification;
    }
}
