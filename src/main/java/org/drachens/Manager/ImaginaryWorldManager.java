package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChunkLoadEvent;
import net.minestom.server.event.player.PlayerPacketOutEvent;
import net.minestom.server.network.packet.server.play.BlockChangePacket;
import org.drachens.dataClasses.ImaginaryWorld;

import java.util.HashMap;

public class ImaginaryWorldManager {
    private final HashMap<Player, ImaginaryWorld> instanceImaginaryWorldHashMap = new HashMap<>();

    public ImaginaryWorldManager() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChunkLoadEvent.class, e -> {
            Player p = e.getPlayer();
            ImaginaryWorld imaginaryWorlds = instanceImaginaryWorldHashMap.get(p);
            if (imaginaryWorlds == null) return;
            imaginaryWorlds.loadChunk(p, e.getInstance().getChunk(e.getChunkX(), e.getChunkZ()));
        });
        MinecraftServer.getGlobalEventHandler().addListener(PlayerPacketOutEvent.class, e -> {
            Player player = e.getPlayer();
            if (instanceImaginaryWorldHashMap.containsKey(player)) {
                if (e.getPacket() instanceof BlockChangePacket blockChangePacket) {
                    ImaginaryWorld imaginaryWorld = instanceImaginaryWorldHashMap.get(player);
                    Pos pos = new Pos(blockChangePacket.blockPosition());
                    if (imaginaryWorld.isBlockUpdatesFromPlayers() &&
                            imaginaryWorld.isThereAGhostBlock(pos) && imaginaryWorld.getGhostBlockPacket(pos) != blockChangePacket) {
                        e.setCancelled(true);
                    }
                }
            }
        });

    }

    public void addPlayers(Player player, ImaginaryWorld imaginaryWorld) {
        instanceImaginaryWorldHashMap.put(player, imaginaryWorld);
    }

    public void removePlayers(Player player, ImaginaryWorld imaginaryWorld) {
        instanceImaginaryWorldHashMap.remove(player);
    }
}
