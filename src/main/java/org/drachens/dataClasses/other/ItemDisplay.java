package org.drachens.dataClasses.other;

import dev.ng5m.CPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import net.minestom.server.utils.PacketUtils;
import org.drachens.animation.AnimationType;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.territories.Province;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.OtherUtil.yawToQuat;


public class ItemDisplay extends Clientside {
    ItemStack item;
    byte displayType;
    private AnimationType active;
    private EntityTeleportPacket entityTeleportPacket;

    public ItemDisplay(ItemStack item, Pos pos, DisplayType displayType, Instance instance, boolean storeViewers) {
        super(storeViewers, instance, pos);

        this.item = item;
        this.displayType = displayType.getSerialized();
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, false);
    }

    public ItemDisplay(ItemStack item, Province province, DisplayType displayType, boolean storeViewers) {
        super(storeViewers, province.getInstance(), province.getPos());

        this.item = item;
        this.displayType = displayType.getSerialized();
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, false);
    }

    public ItemDisplay(ItemStack item, Pos pos, Instance instance, DisplayType displayType, boolean storeViewers) {
        super(storeViewers, instance, pos);

        this.item = item;
        this.displayType = displayType.getSerialized();
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, false);
    }

    public void delete() {
        this.dispose();
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        this.pos = pos;
        entityTeleportPacket = new EntityTeleportPacket(this.entityId, this.pos, false);
        PacketUtils.sendGroupedPacket(getAsPlayers(), entityTeleportPacket);

    }

    public void setActive(AnimationType active) {
        cancelAnimation();
        this.active = active;
    }

    private void cancelAnimation() {
        if (active != null) active.stop(this);
    }

    public void setItem(ItemStack item) {
        this.item = item;
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();


        map.put(23, Metadata.ItemStack(item));
        map.put(24, Metadata.Byte(displayType));
        EntityMetaDataPacket entityMetaDataPacket1 = new EntityMetaDataPacket(
                this.entityId,
                map
        );

        PacketUtils.sendGroupedPacket(getAsPlayers(), entityMetaDataPacket1);
    }

    public void moveSmooth(Province to, int time) {
        moveSmooth(to.getPos(), time);
    }

    public void moveSmooth(Pos to, int time) {
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();
        to = to.add(0.5, 0, 0.5);
        double yaw = Math.atan2(to.x() - pos.x(), to.z() - pos.z()) * (180 / Math.PI);

        float[] quart = yawToQuat(yaw);

        map.put(8, Metadata.VarInt(0));
        map.put(9, Metadata.VarInt(time));//duration in ticks
        map.put(11, Metadata.Vector3(to.sub(pos.x(), 0, pos.z())));
        map.put(13, Metadata.Quaternion(quart));
        map.put(14, Metadata.Quaternion(quart));

        EntityMetaDataPacket entityMetaDataPacket1 = new EntityMetaDataPacket(entityId, map);
        PacketUtils.sendGroupedPacket(getAsPlayers(), entityMetaDataPacket1);
    }

    public void setPosWithOffset(Province province) {
        Pos pos = province.getPos();
        pos = pos.add(0.5, 1.5, 0.5);
        setPos(pos);
    }

    @Override
    public void addCountry(Country country) {
        List<CPlayer> players = country.getPlayer();
        if (storeViewers)
            addPlayers(players);

        List<Player> players1 = new ArrayList<>(players);
        PacketUtils.sendGroupedPacket(players1, new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.ITEM_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();

        map.put(8, Metadata.VarInt(-1));
        map.put(11, Metadata.Vector3(new Pos(0, 0, 0)));
        map.put(23, Metadata.ItemStack(item));
        map.put(24, Metadata.Byte(displayType));

        PacketUtils.sendGroupedPacket(players1, new EntityMetaDataPacket(
                this.entityId,
                map
        ));

        PacketUtils.sendGroupedPacket(players1, entityTeleportPacket);
    }

    @Override
    public void removeCountry(Country country) {
        List<CPlayer> players = country.getPlayer();
        List<Player> players1 = new ArrayList<>(players);
        if (storeViewers)
            removePlayers(players);

        PacketUtils.sendGroupedPacket(players1, new DestroyEntitiesPacket(
                this.entityId
        ));
    }

    @Override
    public void addViewer(CPlayer p) {
        if (storeViewers)
            addPlayer(p);

        PacketUtils.sendPacket(p, new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.ITEM_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();

        map.put(8, Metadata.VarInt(-1));
        map.put(11, Metadata.Vector3(new Pos(0, 0, 0)));
        map.put(23, Metadata.ItemStack(item));
        map.put(24, Metadata.Byte(displayType));

        PacketUtils.sendPacket(p, new EntityMetaDataPacket(
                this.entityId,
                map
        ));

        PacketUtils.sendPacket(p, entityTeleportPacket);

    }

    @Override
    public void removeViewer(CPlayer p) {
        if (storeViewers)
            removePlayer(p);

        PacketUtils.sendPacket(p, new DestroyEntitiesPacket(
                this.entityId
        ));
    }

    @Override
    public SpawnEntityPacket getSpawnPacket() {
        return new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.ITEM_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        );
    }

    @Override
    public DestroyEntitiesPacket getDestroyPacket() {
        return new DestroyEntitiesPacket(
                this.entityId
        );
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
