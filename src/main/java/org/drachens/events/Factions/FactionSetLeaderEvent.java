package org.drachens.events.Factions;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.interfaces.Event;

public class FactionSetLeaderEvent extends Event {
    private final Factions factions;
    private final Country country;

    public FactionSetLeaderEvent(Factions faction, Country country) {
        super(country.getInstance());
        this.factions = faction;
        this.country = country;
    }

    public Factions getFactions() {
        return factions;
    }

    public Country getCountry() {
        return country;
    }
}

