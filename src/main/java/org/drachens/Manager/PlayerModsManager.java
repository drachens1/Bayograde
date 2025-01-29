package org.drachens.Manager;

import org.drachens.player_types.CPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.client.common.ClientPluginMessagePacket;
import org.drachens.dataClasses.packets.PacketReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerModsManager {
    private final HashMap<Instance, List<CPlayer>> players = new HashMap<>();

    public PlayerModsManager() {
        MinecraftServer.getPacketListenerManager().setListener(ClientPluginMessagePacket.class, ((packet, p) -> {
            PacketReader packetReader = new PacketReader(packet);
            if ("continentalmod:valid".equals(packetReader.getChannel())) {
                p.sendPluginMessage(packet.channel(), packet.data());
                CPlayer player = (CPlayer) p;
                player.setIsUsingMod(true);
                System.out.println("Is using mod : " + packetReader.getChannel());
                putPlayer(player, p.getInstance());
            }
        }));
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
