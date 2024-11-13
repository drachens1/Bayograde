package org.drachens.events.Countries;

import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class CountryLeaveEvent extends Event {
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
}