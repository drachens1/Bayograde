package org.drachens.events.Countries;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class CountrySetLeaderEvent extends Event {
    private final Country country;
    private final CPlayer newLeader;

    public CountrySetLeaderEvent(Country country, CPlayer newLeader) {
        super(newLeader.getInstance());
        this.country = country;
        this.newLeader = newLeader;
    }

    public Country getCountry() {
        return country;
    }

    public CPlayer getNewLeader() {
        return newLeader;
    }
}