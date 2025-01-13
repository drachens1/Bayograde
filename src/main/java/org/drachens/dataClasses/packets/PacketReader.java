package org.drachens.dataClasses.packets;

import net.minestom.server.network.packet.client.common.ClientPluginMessagePacket;

public class PacketReader {
    private final String channel;
    private final byte[] data;
    public PacketReader(ClientPluginMessagePacket clientPluginMessagePacket){
        channel=clientPluginMessagePacket.channel();
        this.data=clientPluginMessagePacket.data();
    }

    public String getChannel(){
        return channel;
    }

    public boolean getBoolean(){
        return data[0]==1;
    }
}
