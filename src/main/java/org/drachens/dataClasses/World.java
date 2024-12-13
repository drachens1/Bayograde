package org.drachens.dataClasses;

import dev.ng5m.CPlayer;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;

public abstract class World {
    private final Instance instance;
    private final InstanceContainer instanceContainer;

    public World(InstanceContainer instance){
        this.instance=instance;
        this.instanceContainer=instance;
        instance.setChunkSupplier(LightingChunk::new);
    }

    public abstract void addPlayer(CPlayer p);

    public abstract void removePlayer(CPlayer p);

    public abstract void playerMove(PlayerMoveEvent e);

    public abstract void playerBlockInteract(PlayerBlockInteractEvent e);

    public abstract void playerUseItem(PlayerUseItemEvent e);

    public abstract void playerStartDigging(PlayerStartDiggingEvent e);

    public abstract void playerDisconnect(PlayerDisconnectEvent e);

    public Instance getInstance(){
        return instance;
    }

    public InstanceContainer getInstanceContainer(){
        return instanceContainer;
    }
}
