package org.drachens.events.Countries;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.dataClasses.Countries.Country;

public record CountryCoopPlayerEvent(Country inviter, CPlayer p) implements Event, CancellableEvent {

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}