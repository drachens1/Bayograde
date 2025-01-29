package org.drachens.events.countries;

import org.drachens.player_types.CPlayer;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class CountryJoinEvent extends Event {
    private final Country joined;
    private final CPlayer p;

    public CountryJoinEvent(Country joined, CPlayer p) {
        super(p.getInstance());
        this.joined = joined;
        this.p = p;
    }

    public CPlayer getP() {
        return p;
    }

    public Country getJoined() {
        return joined;
    }
}
