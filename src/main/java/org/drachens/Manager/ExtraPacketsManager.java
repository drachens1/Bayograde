package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.network.ConnectionState;
import net.minestom.server.network.packet.client.play.ClientHeldItemChangePacket;
import org.drachens.events.other.PlayerChangeActiveItemEvent;

public class ExtraPacketsManager {
    public ExtraPacketsManager(){
        MinecraftServer.getPacketListenerManager().setListener(ConnectionState.PLAY, ClientHeldItemChangePacket.class, (packet, player) -> {
            EventDispatcher.call(new PlayerChangeActiveItemEvent(player.getPlayer(),packet.slot()));
        });
    }
}
