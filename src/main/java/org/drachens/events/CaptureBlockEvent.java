package org.drachens.events;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;

public class CaptureBlockEvent implements Event, CancellableEvent {
    private final Country aggressor;
    private final Country occupier;
    private final Province attacked;

    public CaptureBlockEvent(Country aggressor, Country occupier, Province attacked) {
        this.aggressor = aggressor;
        this.occupier = occupier;
        this.attacked = attacked;
    }

    public Country getAggressor() {
        return aggressor;
    }

    public Country getOccupier() {
        return occupier;
    }

    public Province getAttacked() {
        return attacked;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
