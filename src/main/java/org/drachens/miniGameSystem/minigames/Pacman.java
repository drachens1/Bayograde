package org.drachens.miniGameSystem.minigames;

import dev.ng5m.CPlayer;
import dev.ng5m.Util;
import dev.ng5m.util.Direction;
import dev.ng5m.util.MiniGameUtil;
import dev.ng5m.util.VehicleMovementHaccUtil;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientSteerBoatPacket;
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket;
import org.drachens.dataClasses.World;
import org.drachens.miniGameSystem.*;

import java.util.*;

import static java.lang.Math.floor;
import static java.lang.Math.min;

public final class Pacman extends MiniGame<Pacman.PacmanWorld> {
    private static final String MAP_LAYOUT = "■■■■■■■■■■■■■■■■■■■■■■■■■■■■\n" +
            "■            ■■            ■\n" +
            "■ ■■■■ ■■■■■ ■■ ■■■■■ ■■■■ ■\n" +
            "■ ■  ■ ■   ■ ■■ ■   ■ ■  ■ ■\n" +
            "■ ■■■■ ■■■■■ ■■ ■■■■■ ■■■■ ■\n" +
            "■                          ■\n" +
            "■ ■■■■ ■■ ■■■■■■■■ ■■ ■■■■ ■\n" +
            "■ ■■■■ ■■ ■■■■■■■■ ■■ ■■■■ ■\n" +
            "■      ■■    ■■    ■■      ■\n" +
            "■■■■■■ ■■■■■ ■■ ■■■■■ ■■■■■■\n" +
            "     ■ ■■■■■ ■■ ■■■■■ ■     \n" +
            "     ■ ■■          ■■ ■     \n" +
            "     ■ ■■ ■■■__■■■ ■■ ■     \n" +
            "■■■■■■ ■■ ■      ■ ■■ ■■■■■■\n" +
            "          ■      ■          \n" +
            "■■■■■■ ■■ ■      ■ ■■ ■■■■■■\n" +
            "     ■ ■■ ■■■■■■■■ ■■ ■     \n" +
            "     ■ ■■  READY!  ■■ ■     \n" +
            "     ■ ■■ ■■■■■■■■ ■■ ■     \n" +
            "■■■■■■ ■■ ■■■■■■■■ ■■ ■■■■■■\n" +
            "■            ■■            ■\n" +
            "■ ■■■■ ■■■■■ ■■ ■■■■■ ■■■■ ■\n" +
            "■ ■■■■ ■■■■■ ■■ ■■■■■ ■■■■ ■\n" +
            "■   ■■                ■■   ■\n" +
            "■■■ ■■ ■■ ■■■■■■■■ ■■ ■■ ■■■\n" +
            "■■■ ■■ ■■ ■■■■■■■■ ■■ ■■ ■■■\n" +
            "■      ■■    ■■    ■■      ■\n" +
            "■ ■■■■■■■■■■ ■■ ■■■■■■■■■■ ■\n" +
            "■ ■■■■■■■■■■ ■■ ■■■■■■■■■■ ■\n" +
            "■                          ■\n" +
            "■■■■■■■■■■■■■■■■■■■■■■■■■■■■";

    // REVERSED ⚠
    private static final List<String> MAP_LAYOUT_LINES = Arrays.asList(MAP_LAYOUT.split("\n")).reversed();

    private final Sprite map;

    private final PacmanSprite pacman;

    private final Blinky blinky;
    private final Sprite pinky;
    private final Sprite inky;
    private final Sprite clyde;

    private final CPlayer player;

    public Pacman(CPlayer p) {
        super(p, 28, 31, Material.BLACK_CONCRETE, new PacmanWorld(), new Pos(14, 15, -20));
        this.player = p;

        getWorld().setInstance(this);

        map = new Sprite.Builder()
                .setIdentifier("pacmanMap")
                .setLayout(
                        MAP_LAYOUT
                )
                .setIngredient('■', Material.BLUE_CONCRETE)
//                .setIngredient(' ', Material.HEAVY_CORE)
                .build(new Pos(27, 30, -1), getMonitor());

        pacman = new PacmanSprite();
        
        blinky = new Blinky();
        pinky = makeGhostSprite(new Pos(13, 16, 0), "Pinky", Material.PINK_CONCRETE);
        inky = makeGhostSprite(new Pos(14, 16, 0), "Inky", Material.CYAN_CONCRETE);
        clyde = makeGhostSprite(new Pos(15, 16, 0), "Clyde", Material.LIME_CONCRETE);

        MinecraftServer.getPacketListenerManager().setPlayListener(ClientSteerVehiclePacket.class, (clientSteerVehiclePacket, player) -> {
            if (player != this.player) return;

            final byte flags = clientSteerVehiclePacket.flags();

            if (VehicleMovementHaccUtil.jumping(flags) && isDirectionNotObstructed(Direction.NORTH)) {
                pacman.direction = Direction.NORTH;
            } else if (VehicleMovementHaccUtil.sneaking(flags) && isDirectionNotObstructed(Direction.SOUTH)) {
                pacman.direction = Direction.SOUTH;
            }
        });

        MinecraftServer.getPacketListenerManager().setPlayListener(ClientSteerBoatPacket.class, (clientSteerBoatPacket, player) -> {
            if (clientSteerBoatPacket.rightPaddleTurning() && isDirectionNotObstructed(Direction.WEST)) { // turn left coz inverted
                pacman.direction = Direction.WEST;
            } else if (clientSteerBoatPacket.leftPaddleTurning() && isDirectionNotObstructed(Direction.EAST)) {
                pacman.direction = Direction.EAST;
            }
        });
    }

    public boolean isDirectionNotObstructed(Direction direction) {
        return isDirectionNotObstructed(direction, new Pos(pacman.realX, pacman.realY, 0));
    }

    public boolean isDirectionNotObstructed(Direction direction, Pos origin) {
        int newX = (int) floor(origin.x() + direction.x);
        int newY = (int) floor(origin.y() + direction.y);

        char tile = MAP_LAYOUT_LINES.get(newY).charAt(newX);

        return tile != '■';
    }

    private Sprite makeGhostSprite(Pos pos, String id, Material material) {
        Sprite sprite = new Sprite(pos, getMonitor(), "ghost" + id, arg1 -> Util.noop());
        sprite.addDynamicPixel(RelativePos.ZERO, new DynamicPixel(0, material));

        return sprite;
    }

    private void loseCallback() {
        player.sendMessage(Component.text("Lost"));
    }

    private void mainLoop() {
        if (isDirectionNotObstructed(pacman.direction)) {
            int newX = (int) floor(pacman.realX + pacman.direction.x);
            int newY = (int) floor(pacman.realY + pacman.direction.y);

            pacman.realX = newX;
            pacman.realY = newY;
            pacman.updatePosition();
        }

        blinky.move();
    }

    public Direction rotate90Deg(boolean cw, Direction direction) {
        int rx = cw ? direction.y : -direction.y;
        int ry = cw ? -direction.x : direction.x;

        for (Direction d : Direction.values()) {
            if (d.x == rx && d.y == ry) {
                return d;
            }
        }

        throw new RuntimeException("what the fuck bro");
    }

    class Blinky extends Ghost {
        public Blinky() {
            super(new Pos(12, 16, 0), "blinky", new Pos(3, 35, 0), Material.RED_CONCRETE);
        }

        @Override
        protected void moveChase() {

        }
    }

    abstract class Ghost extends Sprite {
        public State state = State.SCATTER;
        public final Pos scatterTargetTile;
        public Pos targetTile;

        public Direction facing = Direction.NORTH;
        public Direction previousDirection = Direction.NORTH;

        public double realX;
        public double realY;

        public Ghost(Pos pos, String identifier, Pos scatterTargetTile, Material material) {
            super(pos, getMonitor(), identifier, (collided, pos1) -> Util.noop());

            addDynamicPixel(new RelativePos(0, 0), new DynamicPixel(0, material));

            this.realX = pos.x();
            this.realY = pos.y();

            this.scatterTargetTile = scatterTargetTile;
            this.targetTile = scatterTargetTile;
        }

        private void moveScatter() {
            var directions = pickDirectionsToMove();
            Direction direction = pickDirectionToMove(directions);
//            while (direction == rotate90Deg(true, rotate90Deg(true, facing)))

            previousDirection = direction;
            facing = direction;

            realX += direction.x;
            realY += direction.y;
        }

        private double squaredDistanceToTargetTile(Direction direction) {
            return new Pos(realX + direction.x, realY + direction.y, 0).distanceSquared(
                    targetTile
            );
        }

        private Direction pickDirectionToMove(List<Direction> validDirections) {
            List<Direction> closest = new ArrayList<>();

            double minDistance = Double.MAX_VALUE;
            for (Direction d : validDirections) {
                double distance = squaredDistanceToTargetTile(d);

                if (distance < minDistance) {
                    minDistance = distance;
                    closest.clear();
                    closest.add(d);
                } else if (distance == minDistance) {
                    closest.add(d);
                }
            }

            Direction f = closest.get(new Random().nextInt(closest.size()));

            while (f == rotate90Deg(true, rotate90Deg(true, facing))) {
                closest.remove(f);
                f = closest.get(new Random().nextInt(closest.size()));
            }

            return f;
        }

        private List<Direction> pickDirectionsToMove() {
            List<Direction> directions = new ArrayList<>();

            if (isDirectionNotObstructed(facing, getPos())) {
                directions.add(facing);
            }

            Direction cw = rotate90Deg(true, facing);
            if (isDirectionNotObstructed(cw, getPos())) {
                directions.add(cw);
            }

            Direction ccw = rotate90Deg(false, facing);
            if (isDirectionNotObstructed(ccw, getPos())) {
                directions.add(ccw);
            }

            return directions;
        }

        protected abstract void moveChase();

        public final void move() {
            switch (state) {
                case SCATTER -> moveScatter();
                case CHASE -> moveChase();
            }

            updatePosition();
        }

        public void updatePosition() {
            setPos(new Pos(realX, realY, 0));
        }

        public enum State {
            SCATTER,
            CHASE;
        }
    }

    class PacmanSprite extends Sprite {
        public double realX = 14;
        public double realY = 7;
        public Direction direction = Direction.WEST;

        public PacmanSprite() {
            super(new Pos(14, 7, 0), Pacman.this.getMonitor(), "pacman", collided -> {
                if (!collided.getIdentifier().startsWith("ghost")) return;

                Pacman.this.loseCallback();
            });

            addDynamicPixel(new RelativePos(0, 0), new DynamicPixel(3, Material.YELLOW_CONCRETE));
        }

        public void updatePosition() {
            setPos(new Pos(realX, realY, 0));
        }
    }

    static class PacmanWorld extends World {
        private Pacman instance;

        public PacmanWorld() {
            super(MinecraftServer.getInstanceManager().createInstanceContainer());
        }

        public void setInstance(Pacman instance) {
            this.instance = instance;
        }

        @Override
        public void addPlayer(CPlayer p) {
            MiniGameUtil.putPlayerInBoat(p, getInstance());
            MiniGameUtil.startGameLoop(instance, 2, instance::mainLoop);
        }

        @Override
        public void removePlayer(CPlayer p) {

        }

        @Override
        public void playerMove(PlayerMoveEvent e) {
            CPlayer p = (CPlayer) e.getPlayer();
        }
    }


}