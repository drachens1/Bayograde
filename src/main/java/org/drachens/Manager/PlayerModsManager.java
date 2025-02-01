package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.instance.Instance;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerModsManager {
    private final HashMap<Instance, List<CPlayer>> players = new HashMap<>();

    public PlayerModsManager() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerPluginMessageEvent.class, e->{
            Player p = e.getPlayer();
            String channel = e.getIdentifier()+e.getMessageString();
            if ("continentalmod:valid".equals(channel)) {
                p.sendPluginMessage(e.getIdentifier(), e.getMessage());
                CPlayer player = (CPlayer) p;
                player.setIsUsingMod(true);
                System.out.println("Is using mod : " + player);
                putPlayer(player, p.getInstance());
            }
        });
    }

    public List<CPlayer> getPlayers(Instance instance) {
        return players.getOrDefault(instance, new ArrayList<>());
    }

    public void putPlayer(CPlayer player, Instance instance) {
        List<CPlayer> p = players.getOrDefault(instance, new ArrayList<>());
        p.add(player);
        players.put(instance, p);
    }

    public void removePlayer(CPlayer player, Instance instance) {
        List<CPlayer> p = players.getOrDefault(instance, new ArrayList<>());
        if (p.remove(player)) {
            players.put(instance, p);
        } else {
            players.remove(instance);
        }
    }
}
