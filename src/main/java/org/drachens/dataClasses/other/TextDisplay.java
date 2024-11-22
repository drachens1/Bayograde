package org.drachens.dataClasses.other;

import net.kyori.adventure.audience.Audience;
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
import net.minestom.server.utils.PacketUtils;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;

import java.time.temporal.ChronoUnit;
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

    private TextDisplay(create c) {
        super(c.storeViewers, c.instance, c.pos);
        this.text = c.text;
        this.lineWidth = c.lineWidth;
        this.backgroundColor = c.backgroundColor;
        this.opacity = c.opacity;
        this.bitmask = c.bitmask;
        this.followPlayer = c.followPlayer;
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, false);
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

        PacketUtils.sendGroupedPacket(VIEWERS, new EntityMetaDataPacket(entityId, map));
    }

    public void destroy(Long delay) {
        MinecraftServer.getSchedulerManager().buildTask(this::dispose).delay(delay, ChronoUnit.MILLIS).schedule();
    }

    public void setText(Component text) {
        this.text = text;
        HashMap<Integer, Metadata.Entry<?>> map = new HashMap<>();
        map.put(23, Metadata.Chat(text));
        EntityMetaDataPacket entityMetaDataPacket1 = new EntityMetaDataPacket(entityId, map);
        if (storeViewers) PacketUtils.sendPacket((Audience) VIEWERS, entityMetaDataPacket1);
        createEntityMetaDataPacket();
    }

    public void setPos(Pos pos) {
        this.pos = pos;
        entityTeleportPacket = new EntityTeleportPacket(entityId, pos, false);
        if (storeViewers) PacketUtils.sendPacket((Audience) VIEWERS, entityTeleportPacket);
    }

    @Override
    public void addCountry(Country country) {
        List<Player> players = country.getPlayer();
        if (storeViewers)
            VIEWERS.addAll(players);

        PacketUtils.sendGroupedPacket(players, new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.TEXT_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        PacketUtils.sendGroupedPacket(players, entityMetaDataPacket);

        PacketUtils.sendGroupedPacket(players, entityTeleportPacket);
    }

    @Override
    public void removeCountry(Country country) {
        List<Player> players = country.getPlayer();
        if (storeViewers)
            VIEWERS.removeAll(players);

        PacketUtils.sendGroupedPacket(players, new DestroyEntitiesPacket(this.entityId));
    }

    @Override
    public void addViewer(Player p) {
        if (storeViewers)
            VIEWERS.add(p);

        PacketUtils.sendPacket(p, new SpawnEntityPacket(
                entityId,
                uuid,
                EntityType.TEXT_DISPLAY.id(),
                pos,
                0f, 0, (short) 0, (short) 0, (short) 0
        ));

        PacketUtils.sendPacket(p, entityMetaDataPacket);

        PacketUtils.sendPacket(p, entityTeleportPacket);
    }

    @Override
    public void removeViewer(Player p) {
        if (storeViewers)
            VIEWERS.remove(p);

        PacketUtils.sendPacket(p, new DestroyEntitiesPacket(this.entityId));
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