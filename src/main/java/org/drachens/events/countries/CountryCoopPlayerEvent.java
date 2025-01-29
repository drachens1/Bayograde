package org.drachens.events.countries;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;
import org.drachens.player_types.CPlayer;

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