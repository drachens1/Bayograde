package org.drachens.events.ranks;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import org.drachens.store.other.Rank;

public record RankAddEvent(Player player, Rank rank) implements Event {
}
