package org.drachens.events.Factions;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.interfaces.Event;

public class FactionJoinEvent extends Event {
    private final Factions factions;
    private final Country country;

    public FactionJoinEvent(Factions factions, Country country) {
        super(country.getInstance());
        this.factions = factions;
        this.country = country;
    }

    public Factions getFactions() {
        return factions;
    }

    public Country getCountry() {
        return country;
    }
}

