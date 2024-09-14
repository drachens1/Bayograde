package org.drachens.dataClasses.other;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;

import java.util.HashMap;


public class ItemDisplay extends Clientside {
    final ItemStack item;
    final Pos pos;
    final byte displayType;

    public ItemDisplay(ItemStack item, Pos pos, DisplayType displayType, boolean storeViewers) {
        super(storeViewers);

        this.item = item;
        this.pos = pos;
        this.displayType = displayType.getSerialized();
    }

    @Override
    public void addViewer(Player player) {
        if (storeViewers)
            VIEWERS.add(player.getUuid());

        var conn = player.getPlayerConnection();

        conn.sendPacket(new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.ITEM_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();

        map.put(23, Metadata.ItemStack(item));
        map.put(24, Metadata.Byte(displayType));

        conn.sendPacket(new EntityMetaDataPacket(
                this.entityId,
                map
        ));

        conn.sendPacket(new EntityTeleportPacket(
                this.entityId,
                this.pos,
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

    public enum DisplayType {
        NONE(0),
        THIRD_PERSON_LEFT_HAND(1),
        THIRD_PERSON_RIGHT_HAND(2),
        FIRST_PERSON_LEFT_HAND(3),
        FIRST_PERSON_RIGHT_HAND(4),
        HEAD(5),
        GUI(6),
        GROUND(7),
        FIXED(8)
        ;

        private final byte serialized;

        DisplayType(final int serialized) {
            this.serialized = (byte) serialized;
        }

        public byte getSerialized() {
            return this.serialized;
        }

    }

}
