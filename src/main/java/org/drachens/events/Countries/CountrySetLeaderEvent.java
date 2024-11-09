package org.drachens.events.Countries;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;

public record CountrySetLeaderEvent(Country country, Player newLeader) implements Event, CancellableEvent {

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}