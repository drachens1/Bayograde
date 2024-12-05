package org.drachens.events;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.territories.Province;
import org.drachens.interfaces.Event;

public class CaptureBlockEvent extends Event {
    private final Country aggressor;
    private final Country defender;
    private final Province attacked;

    public CaptureBlockEvent(Country aggressor, Country occupier, Province attacked) {
        super(aggressor.getInstance());
        this.aggressor = aggressor;
        this.defender = occupier;
        this.attacked = attacked;
    }

    public Country getAggressor() {
        return aggressor;
    }

    public Country getDefender() {
        return defender;
    }

    public Province getAttacked() {
        return attacked;
    }
}
