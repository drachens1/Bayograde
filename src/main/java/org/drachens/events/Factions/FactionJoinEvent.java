package org.drachens.events.Factions;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;

public class FactionJoinEvent implements Event, CancellableEvent {
    private final Country country;
    private final Factions faction;

    public FactionJoinEvent(Factions faction, Country country) {
        this.faction = faction;
        this.country = country;
    }

    public Country getCountry() {
        return country;
    }

    public Factions getFaction() {
        return faction;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}

