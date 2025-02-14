package org.drachens.dataClasses;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import org.drachens.player_types.CPlayer;

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

    public void fill(Pos pos1, Pos pos2, Block blockType) {
        int minX = (int) Math.min(pos1.x(), pos2.x());
        int maxX = (int) Math.max(pos1.x(), pos2.x());
        int minY = (int) Math.min(pos1.y(), pos2.y());
        int maxY = (int) Math.max(pos1.y(), pos2.y());
        int minZ = (int) Math.min(pos1.z(), pos2.z());
        int maxZ = (int) Math.max(pos1.z(), pos2.z());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    instance.setBlock(new Pos(x, y, z), blockType);
                }
            }
        }
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
