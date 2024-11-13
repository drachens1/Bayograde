package org.drachens.events;

import dev.ng5m.Rank;
import net.minestom.server.entity.Player;
import org.drachens.interfaces.Event;

public class RankAddEvent extends Event {
    private final Player player;
    private final Rank rank;

    public RankAddEvent(Player player, Rank rank) {
        this.player = player;
        this.rank = rank;
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }
}
