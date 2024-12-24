package org.drachens.events.ranks;

import dev.ng5m.Rank;
import net.minestom.server.entity.Player;
import org.drachens.interfaces.Event;

public class RankRemoveEvent extends Event {
    private final Player player;
    private final Rank rank;

    public RankRemoveEvent(Player player, Rank rank) {
        super(player.getInstance());
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
