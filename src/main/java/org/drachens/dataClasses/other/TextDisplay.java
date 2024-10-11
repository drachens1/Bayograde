package org.drachens.dataClasses.other;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import org.drachens.dataClasses.Provinces.Province;

import java.util.HashMap;

public class TextDisplay extends Clientside {
    public Pos location;
    public Component text;
    public int lineWidth;
    public int backgroundColor;
    public byte opacity;
    public byte bitmask;

    public TextDisplay(Pos location, Instance instance, Component text, int lineWidth, int backgroundColor, byte opacity, byte bitmask, boolean storeViewers) {
        super(storeViewers, instance);

        this.location = location;
        this.text = text;
        this.lineWidth = lineWidth;
        this.backgroundColor = backgroundColor;
        this.opacity = opacity;
        this.bitmask = bitmask;
    }

    public TextDisplay(Province location, Component text, int lineWidth, int backgroundColor, byte opacity, byte bitmask, boolean storeViewers) {
        super(storeViewers, location.getInstance());

        this.location = location.getPos();
        this.text = text;
        this.lineWidth = lineWidth;
        this.backgroundColor = backgroundColor;
        this.opacity = opacity;
        this.bitmask = bitmask;
    }

    public void setText(Component text) {
        this.text = text;
        updateForViewers();
    }
    public void updatePosition(Pos pos){
        this.location = pos;
        updateForViewers();
    }
    @Override
    public void addViewer(Player player) {
        if (storeViewers)
            VIEWERS.add(player.getUuid());

        var conn = player.getPlayerConnection();

        conn.sendPacket(new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.TEXT_DISPLAY.id(),
                location,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();

        map.put(23, Metadata.Chat(this.text));
        map.put(24, Metadata.VarInt(this.lineWidth));
        map.put(25, Metadata.VarInt(this.backgroundColor));
        map.put(26, Metadata.Byte(this.opacity));
        map.put(27, Metadata.Byte(this.bitmask));

        conn.sendPacket(new EntityMetaDataPacket(
                this.entityId,
                map
        ));

        conn.sendPacket(new EntityTeleportPacket(
                this.entityId,
                this.location,
                false
        ));
    }

    @Override
    public void removeViewer(Player player) {
        if (storeViewers)
            VIEWERS.remove(player.getUuid());

        var conn = player.getPlayerConnection();

        conn.sendPacket(new DestroyEntitiesPacket(
                this.entityId
        ));
    }
}