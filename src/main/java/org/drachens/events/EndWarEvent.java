package org.drachens.events;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class EndWarEvent extends Event {
    private final Country aggressor;
    private final Country defender;

    public EndWarEvent(Country aggressor, Country occupier) {
        this.aggressor = aggressor;
        this.defender = occupier;
    }

    public Country getAggressor() {
        return aggressor;
    }

    public Country getDefender() {
        return defender;
    }
}
