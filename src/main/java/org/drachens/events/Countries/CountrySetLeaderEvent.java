package org.drachens.events.Countries;

import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class CountrySetLeaderEvent extends Event {
    private final Country country;
    private final Player newLeader;

    public CountrySetLeaderEvent(Country country, Player newLeader) {
        this.country = country;
        this.newLeader = newLeader;
    }

    public Country getCountry() {
        return country;
    }

    public Player getNewLeader() {
        return newLeader;
    }
}