package org.drachens.miniGameSystem.minigames;

import dev.ng5m.CPlayer;
import dev.ng5m.events.EventHandlerProvider;
import dev.ng5m.util.MiniGameUtil;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket;
import org.drachens.dataClasses.World;
import org.drachens.miniGameSystem.MiniGame;
import org.drachens.miniGameSystem.Monitor;
import org.drachens.miniGameSystem.Sprite;

import static java.lang.Math.floor;

public class FlappyBird extends MiniGame
        implements EventHandlerProvider {
    private final Sprite bird;
    private static final double gravity = 0.5;
    private double p = 0;
    private final double realX;

    private final CPlayer player;

    public FlappyBird(CPlayer p, int xMax, int yMax) {
        super(p, xMax, yMax, Material.BLUE_CONCRETE, new FlappyBird.FlappyWorld());
        this.player = p;
        ((FlappyWorld) getWorld()).setInstance(this);

        MinecraftServer.getPacketListenerManager().setPlayListener(ClientSteerVehiclePacket.class, (clientSteerVehiclePacket, player) -> {
            if (player != this.player) return;
            if ((clientSteerVehiclePacket.flags() & 0x01) == 0) return;

            this.p += gravity * 2d;
        });

        this.p = yMax / 2d - 1d;
        this.realX = xMax / 2d;

        pipeSprite(new Pos(realX, yMax, 0), getMonitor());

        bird = new Sprite.Builder().setLayout(
                        " Y\n"
                                + "YYY"
                ).setIngredient('Y', Material.YELLOW_CONCRETE)
                .build(new Pos(this.realX, this.p, 0), getMonitor());
    }

    private static Sprite pipeSprite(Pos pos, Monitor monitor) {
        return new Sprite.Builder()
                .setLayout(
                        """
                                 ###
                                 ###
                                 ###
                                 ###
                                 ###
                                 ###
                                #####"""
                )
                .setIngredient('#', Material.GREEN_CONCRETE)
                .build(pos, monitor);
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
            Entity entity = new Entity(EntityType.BOAT);
            entity.setInstance(this.getInstance(), p.getPosition());
            entity.addPassenger(p);

            MiniGameUtil.startGameLoop(flappyBird, 60, () -> {
                flappyBird.p -= gravity;

                flappyBird.bird.setPos(new Pos(floor(flappyBird.realX), floor(flappyBird.p), 0));
            });
        }

        @Override
        public void removePlayer(CPlayer p) {

        }
    }
}
