package org.drachens.dataClasses;

import dev.ng5m.CPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;

public abstract class World {
    private final Instance instance;
    private final InstanceContainer instanceContainer;
    private final Pos spawnPoint;

    public World(InstanceContainer instance, Pos spawnPoint) {
        this.instance = instance;
        this.instanceContainer = instance;
        this.spawnPoint = spawnPoint;
        instance.setChunkSupplier(LightingChunk::new);
    }

    public abstract void addPlayer(CPlayer p);

    public abstract void removePlayer(CPlayer p);

    public void playerMove(PlayerMoveEvent e) {

    }

    public void playerBlockInteract(PlayerBlockInteractEvent e) {

    }

    public void playerUseItem(PlayerUseItemEvent e) {

    }

    public void playerStartDigging(PlayerStartDiggingEvent e) {

    }

    public void playerDisconnect(PlayerDisconnectEvent e) {

    }

    public void playerAnimationEvent(PlayerHandAnimationEvent e) {

    }

    public Instance getInstance() {
        return instance;
    }

    public InstanceContainer getInstanceContainer() {
        return instanceContainer;
    }

    public Pos getSpawnPoint() {
        return spawnPoint;
    }
}
