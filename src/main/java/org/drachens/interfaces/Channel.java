package org.drachens.interfaces;

import net.minestom.server.event.player.PlayerChatEvent;

public interface Channel {
    void onChat(PlayerChatEvent e);
}
