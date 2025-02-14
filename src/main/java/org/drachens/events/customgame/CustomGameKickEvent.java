package org.drachens.events.customgame;

import net.minestom.server.event.Event;
import org.drachens.player_types.CPlayer;

public record CustomGameKickEvent(CPlayer kicker, CPlayer kicked) implements Event { }
