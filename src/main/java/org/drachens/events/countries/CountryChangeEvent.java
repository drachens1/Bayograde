package org.drachens.events.countries;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;
import org.drachens.player_types.CPlayer;

public class CountryChangeEvent extends Event {
    private final Country joined;
    private final Country left;
    private final CPlayer p;

    public CountryChangeEvent(Country joined, Country left, CPlayer p) {
        super(p.getInstance());
        this.joined = joined;
        this.left = left;
        this.p = p;
    }

    public Country getJoined() {
        return joined;
    }

    public Country getLeft() {
        return left;
    }

    public CPlayer getPlayer() {
        return p;
    }
}