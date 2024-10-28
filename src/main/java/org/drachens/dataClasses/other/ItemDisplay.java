package org.drachens.dataClasses.other;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import net.minestom.server.network.player.PlayerConnection;
import org.drachens.dataClasses.Provinces.Province;

import java.util.HashMap;


public class ItemDisplay extends Clientside {
    ItemStack item;
    Pos pos;
    byte displayType;
    public ItemDisplay(ItemStack item, Pos pos, DisplayType displayType, Instance instance, boolean storeViewers) {
        super(storeViewers, instance);

        this.item = item;
        this.pos = pos;
        this.displayType = displayType.getSerialized();
    }

    public ItemDisplay(ItemStack item, Province province, DisplayType displayType, boolean storeViewers) {
        super(storeViewers, province.getInstance());

        this.item = item;
        this.pos = province.getPos();
        this.displayType = displayType.getSerialized();
    }
    public ItemDisplay(ItemStack item, Pos pos, Instance instance, DisplayType displayType, boolean storeViewers) {
        super(storeViewers, instance);

        this.item = item;
        this.pos = pos;
        this.displayType = displayType.getSerialized();
    }

    public void delete() {
        this.dispose();
    }
    public Pos getPos(){
        return pos;
    }

    public void setItem(ItemStack item) {
        this.item = item;
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();

        map.put(23, Metadata.ItemStack(item));
        map.put(24, Metadata.Byte(displayType));

        VIEWERS.forEach(player ->
                player.sendPacket(new EntityMetaDataPacket(
                        this.entityId,
                        map
                )));
    }

    /*public void moveSmooth(Pos to, int time){
        System.out.println("Move smooth");
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();

        map.put(10,VarInt(time));
        map.put(11,Vector3(to.asVec()));
        map.put(23, Metadata.ItemStack(item));
        map.put(24, Metadata.Byte(displayType));

        VIEWERS.forEach(player ->
                player.sendPacket(new EntityMetaDataPacket(
                        this.entityId,
                        map
                )));
    }*/

    public void setPosWithOffset(Province province){
        Pos pos = province.getPos();
        pos = pos.add(0.5, 1.5, 0.5);
        setPos(pos);
    }
    public void setPos(Pos pos){
        this.pos = pos;
        VIEWERS.forEach(player ->
                player.sendPacket(new EntityTeleportPacket(
                this.entityId,
                this.pos,
                false
                )));

    }
    @Override
    public void addViewer(PlayerConnection player) {
        if (storeViewers)
            VIEWERS.add(player);

        player.sendPacket(new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.ITEM_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();

        map.put(23, Metadata.ItemStack(item));
        map.put(24, Metadata.Byte(displayType));

        player.sendPacket(new EntityMetaDataPacket(
                this.entityId,
                map
        ));

        player.sendPacket(new EntityTeleportPacket(
                this.entityId,
                this.pos,
                false
        ));
    }

    @Override
    public void removeViewer(PlayerConnection player) {
        if (storeViewers)
            VIEWERS.remove(player);

        player.sendPacket(new DestroyEntitiesPacket(
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
        FIXED(8);

        private final byte serialized;

        DisplayType(final int serialized) {
            this.serialized = (byte) serialized;
        }

        public byte getSerialized() {
            return this.serialized;
        }

    }

}
