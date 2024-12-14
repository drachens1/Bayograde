package org.drachens.miniGameSystem.minigames;

import dev.ng5m.CPlayer;
import dev.ng5m.events.EventHandler;
import dev.ng5m.events.EventHandlerProvider;
import dev.ng5m.util.MiniGameUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.*;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.World;
import org.drachens.miniGameSystem.DynamicPixel;
import org.drachens.miniGameSystem.MiniGame;
import org.drachens.miniGameSystem.RelativePos;
import org.drachens.miniGameSystem.Sprite;

import static java.lang.Math.floor;

public class FlappyBird extends MiniGame
        implements EventHandlerProvider {
    private final Sprite bird;
    private static final double gravity = 0.5;
    private double realX = 0;
    private double realY = 0;

    public FlappyBird(CPlayer p, int xMax, int yMax) {
        super(p, xMax, yMax, Material.BLUE_CONCRETE, new FlappyBird.FlappyWorld());
        ((FlappyWorld) getWorld()).setInstance(this);

        realX = xMax - 3;
        realY = yMax / 2d - 1d;

        getMonitor().clear(Material.BLUE_CONCRETE);

        bird = new Sprite.Builder().setLayout(
                  " Y\n"
                + "YYY"
        ).setIngredient('Y', Material.YELLOW_CONCRETE)
                .build(new Pos(realX, realY, 0), getMonitor());
    }

    static class FlappyWorld extends World {

        public FlappyWorld() {
            super(MinecraftServer.getInstanceManager().createInstanceContainer());
        }

        private FlappyBird flappyBird;

        public void setInstance(FlappyBird flappyBird) {
            this.flappyBird = flappyBird;
        }

        @Override
        public void addPlayer(CPlayer p) {
            MiniGameUtil.startGameLoop(flappyBird, 60, () -> {
                flappyBird.realY -= gravity;

                flappyBird.bird.setPos(new Pos(floor(flappyBird.realX), floor(flappyBird.realY), 0));
            });
        }

        @Override
        public void removePlayer(CPlayer p) {

        }
    }
}
