package org.drachens.events.Factions;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.interfaces.Event;

public class FactionCreateEvent extends Event {
    private final Country creator;
    private final Factions newFaction;

    public FactionCreateEvent(Country creator, Factions newFaction) {
        super(creator.getInstance());
        this.creator = creator;
        this.newFaction = newFaction;
    }

    public Country getCreator() {
        return creator;
    }

    public Factions getNewFaction() {
        return newFaction;
    }
}
