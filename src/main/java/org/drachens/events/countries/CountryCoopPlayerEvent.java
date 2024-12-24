package org.drachens.events.countries;

import dev.ng5m.CPlayer;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class CountryCoopPlayerEvent extends Event {
    private final Country inviter;
    private final CPlayer p;

    public CountryCoopPlayerEvent(Country inviter, CPlayer p) {
        super(p.getInstance());
        this.inviter = inviter;
        this.p = p;
    }

    public CPlayer getPlayer() {
        return p;
    }

    public Country getInviter() {
        return inviter;
    }
}