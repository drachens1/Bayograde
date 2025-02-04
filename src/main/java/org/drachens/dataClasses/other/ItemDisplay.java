package org.drachens.dataClasses.other;

import net.minestom.server.MinecraftServer;
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
import net.minestom.server.utils.PacketSendingUtils;
import org.drachens.animation.AnimationType;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.player_types.CPlayer;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.drachens.util.OtherUtil.yawToQuat;


public class ItemDisplay extends Clientside {
    ItemStack item;
    byte displayType;
    private AnimationType active;
    private EntityTeleportPacket entityTeleportPacket;
    private boolean glowing = false;
    private boolean hidden = false;
    private EntityMetaDataPacket interpolation;
    private float yaw = 0f;

    public ItemDisplay(ItemStack item, Pos pos, DisplayType displayType, Instance instance, boolean storeViewers) {
        super(storeViewers, instance, pos);

        this.item = item;
        this.displayType = displayType.getSerialized();
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, pos, 0, false);
    }

    public ItemDisplay(ItemStack item, Province province, DisplayType displayType, boolean storeViewers) {
        super(storeViewers, province.getInstance(), province.getPos());

        this.item = item;
        this.displayType = displayType.getSerialized();
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, pos, 0, false);
    }

    public ItemDisplay(ItemStack item, Pos pos, Instance instance, DisplayType displayType, boolean storeViewers) {
        super(storeViewers, instance, pos);

        this.item = item;
        this.displayType = displayType.getSerialized();
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, pos, 0, false);
    }

    public float getYaw(){
        return yaw;
    }

    public void delete() {
        this.dispose();
    }

    public Pos getPos() {
        return pos;
    }

    public void setPos(Pos pos) {
        pos = pos.withYaw(yaw);
        this.pos = pos;
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, pos, 0, false);
        PacketSendingUtils.sendGroupedPacket(getAsPlayers(), entityTeleportPacket);
    }

    public void setGhostPos(Pos pos){
        pos = pos.withYaw(yaw);
        this.pos=pos;
    }

    public void addYaw(float yaw){
        this.yaw+=yaw;
        setPos(getPos().withYaw(this.yaw));
        if (this.yaw>360f){
            this.yaw-=360f;
        }
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

        PacketSendingUtils.sendGroupedPacket(getAsPlayers(), entityMetaDataPacket1);
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
        PacketSendingUtils.sendGroupedPacket(getAsPlayers(), entityMetaDataPacket1);
        interpolation=entityMetaDataPacket1;
        MinecraftServer.getSchedulerManager().buildTask(()-> interpolation=null).delay(time * 50L,ChronoUnit.MILLIS).schedule();
    }

    public void setGlowing(boolean glowing) {
        this.glowing = glowing;
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();
        if (glowing) {
            map.put(0, Metadata.Byte((byte) 0x40));
        } else
            map.put(0, Metadata.Byte((byte) 0));
        EntityMetaDataPacket entityMetaDataPacket = new EntityMetaDataPacket(entityId, map);
        PacketSendingUtils.sendGroupedPacket(getAsPlayers(), entityMetaDataPacket);
    }

    public void setPosWithOffset(Province province) {
        Pos pos = province.getPos();
        pos = pos.add(0.5, 1.5, 0.5);
        setPos(pos);
    }

    public EntityMetaDataPacket getEntityMetaDataPacket() {
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();

        if (glowing) map.put(0, Metadata.Byte((byte) 0x40));
        map.put(8, Metadata.VarInt(-1));
        map.put(11, Metadata.Vector3(new Pos(0, 0, 0)));
        map.put(23, Metadata.ItemStack(item));
        map.put(24, Metadata.Byte(displayType));
        return new EntityMetaDataPacket(
                this.entityId,
                map
        );
    }

    @Override
    public void addCountry(Country country) {
        List<CPlayer> players = country.getPlayer();
        if (storeViewers)
            addPlayers(players);

        List<Player> players1 = new ArrayList<>(players);
        PacketSendingUtils.sendGroupedPacket(players1, new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.ITEM_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));


        PacketSendingUtils.sendGroupedPacket(players1, getEntityMetaDataPacket());

        PacketSendingUtils.sendGroupedPacket(players1, entityTeleportPacket);

        if (interpolation!=null){
            PacketSendingUtils.sendGroupedPacket(players1,interpolation);
        }
    }

    @Override
    public void removeCountry(Country country) {
        List<CPlayer> players = country.getPlayer();
        List<Player> players1 = new ArrayList<>(players);
        if (storeViewers)
            removePlayers(players);

        PacketSendingUtils.sendGroupedPacket(players1, new DestroyEntitiesPacket(
                this.entityId
        ));
    }

    @Override
    public void addViewer(CPlayer p) {
        if (storeViewers)
            addPlayer(p);

        PacketSendingUtils.sendPacket(p, new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.ITEM_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        PacketSendingUtils.sendPacket(p, getEntityMetaDataPacket());

        PacketSendingUtils.sendPacket(p, entityTeleportPacket);

        if (interpolation!=null){
            PacketSendingUtils.sendPacket(p,interpolation);
        }
    }

    @Override
    public void removeViewer(CPlayer p) {
        if (storeViewers)
            removePlayer(p);

        PacketSendingUtils.sendPacket(p, new DestroyEntitiesPacket(
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

    public void hide(){
        if (hidden)return;
        PacketSendingUtils.sendGroupedPacket(getAsPlayers(),getDestroyPacket());
        hidden=true;
    }

    public void show(){
        if (!hidden)return;
        PacketSendingUtils.sendGroupedPacket(getAsPlayers(),getSpawnPacket());
        hidden=false;
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
