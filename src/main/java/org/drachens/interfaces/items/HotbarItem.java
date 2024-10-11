package org.drachens.interfaces.items;

import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;

public interface HotbarItem {
    void onUse(PlayerUseItemEvent e);
    void onUse(PlayerUseItemOnBlockEvent e);
}
