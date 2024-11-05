package org.drachens.events.Factions;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;

public class FactionInviteEvent implements Event, CancellableEvent {

    private final Country invited;
    private final Factions faction;
    public FactionInviteEvent(Country invited, Factions faction) {
        this.invited = invited;
        this.faction = faction;
    }

    public Country getInvited() {
        return invited;
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
