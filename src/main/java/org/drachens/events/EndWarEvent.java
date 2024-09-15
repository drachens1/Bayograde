package org.drachens.events;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;

public class EndWarEvent implements Event, CancellableEvent {
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

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
