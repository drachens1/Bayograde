package org.drachens.events.customgame;

import net.minestom.server.event.Event;
import org.drachens.player_types.CPlayer;

public record CustomGameInviteEvent(CPlayer inviter, CPlayer invited) implements Event {
}
