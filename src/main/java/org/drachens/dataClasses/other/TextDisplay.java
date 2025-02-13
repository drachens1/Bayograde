package org.drachens.dataClasses.other;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Metadata;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket;
import net.minestom.server.network.packet.server.play.EntityTeleportPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import net.minestom.server.utils.PacketSendingUtils;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.player_types.CPlayer;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextDisplay extends Clientside {
    private final boolean followPlayer;
    public Component text;
    public int lineWidth;
    public int backgroundColor;
    public byte opacity;
    public byte bitmask;
    private EntityMetaDataPacket entityMetaDataPacket;
    private EntityTeleportPacket entityTeleportPacket;
    private boolean hidden = false;

    private TextDisplay(create c) {
        super(c.storeViewers, c.instance, c.pos);
        this.text = c.text;
        this.lineWidth = c.lineWidth;
        this.backgroundColor = c.backgroundColor;
        this.opacity = c.opacity;
        this.bitmask = c.bitmask;
        this.followPlayer = c.followPlayer;
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, pos, 0, false);
        createEntityMetaDataPacket();
    }

    private void createEntityMetaDataPacket() {
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();
        if (followPlayer) map.put(15, Metadata.Byte((byte) 1));

        map.put(8, Metadata.VarInt(-1));
        map.put(11, Metadata.Vector3(new Pos(0, 0, 0)));
        map.put(23, Metadata.Chat(this.text));
        map.put(24, Metadata.VarInt(this.lineWidth));
        map.put(25, Metadata.VarInt(this.backgroundColor));
        map.put(26, Metadata.Byte(this.opacity));
        map.put(27, Metadata.Byte(this.bitmask));

        entityMetaDataPacket = new EntityMetaDataPacket(this.entityId,
                map);
    }

    public void moveNoRotation(Pos to, int ticks, boolean addition) {
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();
        if (!addition) to = to.add(0.5, 0, 0.5);

        map.put(26, Metadata.Byte((byte) 4));
        map.put(8, Metadata.VarInt(0));
        map.put(9, Metadata.VarInt(ticks));//duration in ticks
        if (addition) {
            map.put(11, Metadata.Vector3(to.asVec()));
        } else if (to.y() != 0) {
            map.put(11, Metadata.Vector3(to.sub(pos.x(), pos.y(), pos.z())));
        } else
            map.put(11, Metadata.Vector3(to.sub(pos.x(), 0, pos.z())));

        PacketSendingUtils.sendGroupedPacket(getAsPlayers(), new EntityMetaDataPacket(entityId, map));
    }

    public void destroy(Long delay) {
        MinecraftServer.getSchedulerManager().buildTask(this::dispose).delay(delay, ChronoUnit.MILLIS).schedule();
    }

    public void setText(Component text) {
        this.text = text;
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();
        map.put(23, Metadata.Chat(text));
        EntityMetaDataPacket entityMetaDataPacket1 = new EntityMetaDataPacket(entityId, map);
        if (storeViewers) PacketSendingUtils.sendGroupedPacket(getAsPlayers(), entityMetaDataPacket1);
        createEntityMetaDataPacket();
    }

    public void setPos(Pos pos) {
        this.pos = pos;
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, pos, 0, false);
        if (storeViewers) PacketSendingUtils.sendGroupedPacket(getAsPlayers(), entityTeleportPacket);
    }

    @Override
    public void addCountry(Country country) {
        List<CPlayer> players = country.getPlayers();
        if (storeViewers)
            addPlayers(players);
        if (hidden)return;
        List<Player> players1 = new ArrayList<>(players);

        PacketSendingUtils.sendGroupedPacket(players1, new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.TEXT_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        PacketSendingUtils.sendGroupedPacket(players1, entityMetaDataPacket);

        PacketSendingUtils.sendGroupedPacket(players1, entityTeleportPacket);
    }

    @Override
    public void removeCountry(Country country) {
        List<CPlayer> players = country.getPlayers();
        if (storeViewers)
            removePlayers(players);
        if (hidden)return;

        PacketSendingUtils.sendGroupedPacket(new ArrayList<>(players), new DestroyEntitiesPacket(this.entityId));
    }

    @Override
    public void addViewer(CPlayer p) {
        if (storeViewers)
            addPlayer(p);
        if (hidden)return;

        PacketSendingUtils.sendPacket(p, new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.TEXT_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        PacketSendingUtils.sendPacket(p, entityMetaDataPacket);

        PacketSendingUtils.sendPacket(p, entityTeleportPacket);
    }

    @Override
    public void removeViewer(CPlayer p) {
        if (storeViewers)
            addViewer(p);
        if (hidden)return;

        PacketSendingUtils.sendPacket(p, new DestroyEntitiesPacket(this.entityId));
    }

    @Override
    public SpawnEntityPacket getSpawnPacket() {
        return new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.TEXT_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        );
    }

    @Override
    public DestroyEntitiesPacket getDestroyPacket() {
        return new DestroyEntitiesPacket(this.entityId);
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

    public static class create {
        private final Instance instance;
        private final Component text;
        private final boolean storeViewers = true;
        public int lineWidth = 200;
        public int backgroundColor = 1;
        public byte opacity = 1;
        public byte bitmask = 1;
        private Pos pos;
        private boolean followPlayer = false;

        public create(Pos pos, Instance instance, Component text) {
            this.pos = pos;
            this.instance = instance;
            this.text = text;
        }

        public create(Province province, Component text) {
            this.pos = province.getPos();
            this.instance = province.getInstance();
            this.text = text;
        }

        public create setFollowPlayer(boolean active) {
            this.followPlayer = active;
            return this;
        }

        public create setLineWidth(int lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        public create setBackgroundColour(int colour) {
            this.backgroundColor = colour;
            return this;
        }

        public create setOpacity(byte b) {
            this.opacity = b;
            return this;
        }

        public create setBitMask(byte b) {
            this.bitmask = b;
            return this;
        }

        public create withOffset() {
            pos = pos.add(0.5, 1.5, 0.5);
            return this;
        }

        public TextDisplay build() {
            return new TextDisplay(this);
        }
    }
}