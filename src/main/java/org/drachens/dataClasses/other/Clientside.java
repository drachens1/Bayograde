package org.drachens.dataClasses.other;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.player.PlayerConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.drachens.util.ServerUtil.getWorldClasses;

public abstract class Clientside {
    public static final List<Clientside> INSTANCES = new ArrayList<>();
    public final List<PlayerConnection> VIEWERS = new ArrayList<>();
    public final int entityId;
    public final UUID uuid;
    public final Instance instance;
    public boolean storeViewers;
    public Pos pos;

    public Clientside(boolean storeViewers, Instance instance, Pos pos) {
        this.storeViewers = storeViewers;
        this.entityId = Entity.generateId();
        this.uuid = UUID.randomUUID();
        this.instance = instance;
        this.pos = pos;
        getWorldClasses(instance).clientEntsToLoad().addClientSide(instance, this);
    }
    public void addViewer(Player p){
        addViewer(p.getPlayerConnection());
    }
    public void removeViewer(Player p){
        removeViewer(p.getPlayerConnection());
    }
    public abstract void addViewer(PlayerConnection player);

    public abstract void removeViewer(PlayerConnection player);

    public void updateForViewer(PlayerConnection player) {
        removeViewer(player);
        addViewer(player);
    }

    public void updateForViewers() {
        if (!storeViewers) return;
        new ArrayList<>(VIEWERS).forEach(this::updateForViewer);
    }

    public Clientside dispose() {
        INSTANCES.remove(this);
        if (storeViewers)
            new ArrayList<>(VIEWERS).forEach(this::removeViewer);

        return this;
    }
}
