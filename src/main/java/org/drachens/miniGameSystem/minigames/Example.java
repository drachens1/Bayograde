package org.drachens.miniGameSystem.minigames;

import dev.ng5m.CPlayer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;
import org.drachens.miniGameSystem.*;

public class Example extends MiniGame {
    private final Sprite sprite;
    public Example(CPlayer p, int xMax, int yMax) {
        super(p, xMax, yMax, Material.BLACK_CONCRETE, new ExampleWorld(),new Pos(25,0,25));
        ExampleWorld exampleWorld = (ExampleWorld) getWorld();
        exampleWorld.setMiniGame(this);
        Monitor monitor = getMonitor();
        sprite = new Sprite(new Pos(100, 100, 0), monitor, "WOMP", collided -> {

        });
        sprite.addDynamicPixel(new RelativePos(0,0),new DynamicPixel(10, Material.ORANGE_CONCRETE));
    }
    public Sprite getSprite(){
        return sprite;
    }
}
