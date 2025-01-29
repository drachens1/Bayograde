package org.drachens.dataClasses.other;

import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import org.drachens.animation.AnimationType;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Clientside {
    public static final List<Clientside> INSTANCES = new ArrayList<>();
    public final int entityId;
    public final BoundingBox boundingBox;
    public final UUID uuid;
    public final Instance instance;
    private final List<CPlayer> VIEWERS = new ArrayList<>();
    public boolean storeViewers;
    public Pos pos;
    private AnimationType animation;

    public Clientside(boolean storeViewers, Instance instance, Pos pos) {
        this.storeViewers = storeViewers;
        this.entityId = Entity.generateId();
        this.uuid = UUID.randomUUID();
        this.instance = instance;
        this.pos = pos;
        this.boundingBox = new BoundingBox(
                pos.x() - 0.5, pos.y(), pos.z() - 0.5
        );
    }

    public abstract void addCountry(Country country);

    public abstract void removeCountry(Country country);

    public abstract void addViewer(CPlayer p);

    public abstract void removeViewer(CPlayer p);

    public void dispose() {
        INSTANCES.remove(this);
        if (storeViewers)
            new ArrayList<>(VIEWERS).forEach(this::removeViewer);

    }

    public abstract SpawnEntityPacket getSpawnPacket();

    public abstract DestroyEntitiesPacket getDestroyPacket();

    public void addPlayer(CPlayer p) {
        VIEWERS.add(p);
    }

    public void removePlayer(CPlayer p) {
        VIEWERS.remove(p);
    }

    public void addPlayers(List<CPlayer> p) {
        VIEWERS.addAll(p);
    }

    public void removePlayers(List<CPlayer> p) {
        VIEWERS.removeAll(p);
    }

    public List<CPlayer> getViewers() {
        return VIEWERS;
    }

    public List<Player> getAsPlayers() {
        return new ArrayList<>(VIEWERS);
    }

    public AnimationType getAnimation() {
        return animation;
    }

    public void setAnimation(AnimationType animation) {
        this.animation = animation;
    }
}
