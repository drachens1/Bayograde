package org.drachens.events.Factions;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;

public class FactionCreateEvent implements Event, CancellableEvent {
    private final Country creator;
    private final Factions newFaction;
    public FactionCreateEvent(Country creator, Factions newFaction) {
        this.creator = creator;
        this.newFaction = newFaction;
    }

    public Country getCreator() {
        return creator;
    }

    public Factions getNewFaction() {
        return newFaction;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
