package org.drachens.miniGameSystem;

import dev.ng5m.CPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.World;

public abstract class MiniGame {
    private final Monitor monitor;
    private final World world;

    public MiniGame(CPlayer p, int xMax, int yMax, Material defaultMaterial, World world){
        this.world=world;
        ContinentalManagers.worldManager.registerWorld(world);
        Instance instance = world.getInstance();
        monitor = new Monitor(instance,defaultMaterial);
        for (int x = 0; x < xMax; x++){
            for (int y = 0; y < yMax; y++){
                monitor.addPixel(new Pos(x,y,0),new Pixel(Material.BLACK_CONCRETE,new Pos(x,y,0),monitor));
            }
        }

        instance.setBlock(new Pos(25,15,-25), Block.BLACK_CONCRETE);
        p.setInstance(instance,new Pos(25,16,-25));
    }

    public Monitor getMonitor(){
        return monitor;
    }

    public Instance getInstance(){
        return world.getInstance();
    }

    public World getWorld(){
        return world;
    }
}
