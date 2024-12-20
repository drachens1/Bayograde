package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerChunkLoadEvent;
import org.drachens.dataClasses.ImaginaryWorld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImaginaryWorldManager {
    private final HashMap<Player, List<ImaginaryWorld>> instanceImaginaryWorldHashMap = new HashMap<>();
    public ImaginaryWorldManager(){
        MinecraftServer.getGlobalEventHandler().addListener(PlayerChunkLoadEvent.class,e->{
            Player p = e.getPlayer();
            List<ImaginaryWorld> imaginaryWorlds = instanceImaginaryWorldHashMap.get(p);
            if (imaginaryWorlds==null)return;
            imaginaryWorlds.forEach(imaginaryWorld -> imaginaryWorld.loadChunk(p,e.getInstance().getChunk(e.getChunkX(),e.getChunkZ())));
        });
    }

    public void addPlayers(Player player, ImaginaryWorld imaginaryWorld){
        List<ImaginaryWorld> imaginaryWorlds = instanceImaginaryWorldHashMap.getOrDefault(player, new ArrayList<>());
        imaginaryWorlds.add(imaginaryWorld);
        instanceImaginaryWorldHashMap.put(player,imaginaryWorlds);
    }

    public void removePlayers(Player player, ImaginaryWorld imaginaryWorld){
        List<ImaginaryWorld> imaginaryWorlds = instanceImaginaryWorldHashMap.getOrDefault(player, new ArrayList<>());
        imaginaryWorlds.remove(imaginaryWorld);
        instanceImaginaryWorldHashMap.put(player,imaginaryWorlds);
    }
}
