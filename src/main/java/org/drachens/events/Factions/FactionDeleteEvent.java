package org.drachens.events.Factions;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
public class FactionDeleteEvent implements Event, CancellableEvent {
    private final Country deleter;
    private final Factions deletedFaction;

    public FactionDeleteEvent(Country deleter, Factions deletedFaction) {
        this.deleter = deleter;
        this.deletedFaction = deletedFaction;
    }

    public Country getDeleter() {
        return deleter;
    }

    public Factions getDeletedFaction() {
        return deletedFaction;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
