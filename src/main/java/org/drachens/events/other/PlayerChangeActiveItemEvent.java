package org.drachens.events.other;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;

public record PlayerChangeActiveItemEvent(Player player, int slot) implements Event { }