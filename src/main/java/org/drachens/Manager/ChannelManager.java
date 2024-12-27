package org.drachens.Manager;

import dev.ng5m.CPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerChatEvent;
import org.drachens.interfaces.Channel;

import java.util.HashMap;

public class ChannelManager {
    private final HashMap<CPlayer, Channel> channelHashMap = new HashMap<>();

    public ChannelManager(){
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChatEvent.class, e->{
            CPlayer p = (CPlayer) e.getPlayer();
            if (!channelHashMap.containsKey(p))return;
            e.setCancelled(true);
            channelHashMap.get(p).onChat(e);
        });
    }

    public void putPlayer(CPlayer player, Channel channel){
        channelHashMap.put(player,channel);
    }

    public void removePlayer(CPlayer player){
        channelHashMap.remove(player);
    }
}
