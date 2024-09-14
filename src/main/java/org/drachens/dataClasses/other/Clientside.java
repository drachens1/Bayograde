package org.drachens.dataClasses.other;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Clientside {
    public static final List<Clientside> INSTANCES = new ArrayList<>();
    public final List<UUID> VIEWERS = new ArrayList<>();

    public boolean storeViewers;
    public final int entityId;
    public final UUID uuid;

    public Clientside(boolean storeViewers) {
        this.storeViewers = storeViewers;
        this.entityId = Entity.generateId();
        this.uuid = UUID.randomUUID();
    }

    public abstract void addViewer(Player player);
    public abstract void removeViewer(Player player);

    public void updateForViewer(Player player) {
        removeViewer(player);
        addViewer(player);
    }

    public void updateForViewers() {
        if (!storeViewers) return;

        for (var viewer : VIEWERS)
            updateForViewer(MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(viewer));
    }

    public Clientside dispose() {
        INSTANCES.remove(this);
        if (storeViewers)
            for (var viewer : this.VIEWERS)
                removeViewer(MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(viewer));

        return this;
    }
}
