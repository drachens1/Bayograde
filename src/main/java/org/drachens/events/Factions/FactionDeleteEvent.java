package org.drachens.events.Factions;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.interfaces.Event;

public class FactionDeleteEvent extends Event {
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
}
