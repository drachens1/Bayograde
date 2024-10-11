package org.drachens.interfaces;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;

public interface War {
    void onClick(PlayerBlockInteractEvent e);
    void onClick(PlayerUseItemEvent e);
    void onClick(PlayerStartDiggingEvent e);
}
