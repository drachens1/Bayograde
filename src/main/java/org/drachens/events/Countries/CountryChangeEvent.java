package org.drachens.events.Countries;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;

public class CountryChangeEvent implements Event, CancellableEvent {
    private final Country joined;
    private final Country left;
    private final CPlayer p;

    public CountryChangeEvent(Country joined, Country left, CPlayer p) {
        this.joined = joined;
        this.left = left;
        this.p = p;
    }

    public CPlayer getP() {
        return p;
    }

    public Country getJoined() {
        return joined;
    }

    public Country getLeft() {
        return left;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}