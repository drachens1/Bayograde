package org.drachens.events;

import net.minestom.server.event.Event;
import org.drachens.player_types.CPlayer;

public record AdvancementEvent(CPlayer p, String identifier) implements Event { }
