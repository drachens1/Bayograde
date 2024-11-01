package org.drachens.events.Countries;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;

public class CountryJoinEvent implements Event, CancellableEvent {
    private final Country joined;
    private final CPlayer p;

    public CountryJoinEvent(Country joined, CPlayer p) {
        this.joined = joined;
        this.p = p;
    }

    public CPlayer getP() {
        return p;
    }

    public Country getJoined() {
        return joined;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
