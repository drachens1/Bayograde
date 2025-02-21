package org.drachens.miniGameSystem;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.World;
import org.drachens.player_types.CPlayer;

public abstract class MiniGame<W extends World> {
    private final Monitor monitor;
    private final W world;

    protected MiniGame(CPlayer p, int xMax, int yMax, Material defaultMaterial, W world) {
        this.world = world;
        ContinentalManagers.worldManager.registerWorld(world);
        Instance instance = world.getInstance();
        monitor = new Monitor(instance, defaultMaterial);
        for (int x = 0; x < xMax; x++) {
            for (int y = 0; y < yMax; y++) {
                monitor.addPixel(new Pos(x, y, 0), new Pixel(defaultMaterial, new Pos(x, y, 0), monitor));
            }
        }
        Pos playerSpawning = world.getSpawnPoint();
        instance.setBlock(playerSpawning.add(0, -1, 0), Block.BLACK_CONCRETE);
        p.setInstance(instance, playerSpawning.add(0, 2, 0));
    }

    public Monitor getMonitor() {
        return monitor;
    }

    public Instance getInstance() {
        return world.getInstance();
    }

    public W getWorld() {
        return world;
    }
}
