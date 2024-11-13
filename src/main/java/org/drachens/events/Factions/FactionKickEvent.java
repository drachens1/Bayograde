package org.drachens.events.Factions;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.interfaces.Event;

public class FactionKickEvent extends Event {
    private final Country country;
    private final Factions faction;

    public FactionKickEvent(Factions faction, Country country) {
        this.faction = faction;
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    public Factions getFaction() {
        return faction;
    }
}

