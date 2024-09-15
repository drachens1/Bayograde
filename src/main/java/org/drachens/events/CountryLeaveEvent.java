package org.drachens.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;

public class CountryLeaveEvent implements Event, CancellableEvent {
    private final Country left;
    private final Player p;

    public CountryLeaveEvent(Country left, Player p) {
        this.left = left;
        this.p = p;
    }

    public Player getP() {
        return p;
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