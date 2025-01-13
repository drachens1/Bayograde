package org.drachens.Manager;

import dev.ng5m.CPlayer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.network.packet.client.common.ClientPluginMessagePacket;
import org.drachens.dataClasses.packets.PacketReader;

public class PlayerModsManager {
    public PlayerModsManager(){
        MinecraftServer.getPacketListenerManager().setListener(ClientPluginMessagePacket.class, ((packet, p) -> {
            PacketReader packetReader = new PacketReader(packet);
            if ("continentalmod:valid".equals(packetReader.getChannel())) {
                p.sendPluginMessage(packet.channel(),packet.data());
                CPlayer player = (CPlayer) p;
                player.setIsUsingMod(true);
                System.out.println("Is using mod");
            }
        }));
    }
}
