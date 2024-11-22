package org.drachens.events.Factions;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.interfaces.Event;

public class FactionInviteEvent extends Event {

    private final Country invited;
    private final Factions faction;

    public FactionInviteEvent(Country invited, Factions faction) {
        super(invited.getInstance());
        this.invited = invited;
        this.faction = faction;
    }

    public Country getInvited() {
        return invited;
    }

    public Factions getFaction() {
        return faction;
    }
}
