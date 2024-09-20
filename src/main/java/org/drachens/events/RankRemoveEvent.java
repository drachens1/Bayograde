package org.drachens.events;

import dev.ng5m.Rank;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;

public class RankRemoveEvent implements Event, CancellableEvent {
    private final Player player;
    private final Rank rank;

    public RankRemoveEvent(Player player, Rank rank) {
        this.player = player;
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
