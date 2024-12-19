package org.drachens.miniGameSystem.minigames;

import dev.ng5m.CPlayer;
import dev.ng5m.Util;
import dev.ng5m.util.Direction;
import dev.ng5m.util.MiniGameUtil;
import dev.ng5m.util.VehicleMovementHaccUtil;
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
            "■■■■■■ ■■ ■xx x x■ ■■ ■■■■■■\n" +
            "          ■x  xxx■          \n" +
            "■■■■■■ ■■ ■xxxxxx■ ■■ ■■■■■■\n" +
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
    private final Pinky pinky;
    private final Inky inky;
    private final Clyde clyde;

    private final CPlayer player;

    private static final int xMax = 28;
    private static final int yMax = 31;

    public Pacman(CPlayer p) {
        super(p, xMax, yMax, Material.BLACK_CONCRETE, new PacmanWorld(), new Pos(14, 15, -20));
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
        pinky = new Pinky();
        inky = new Inky();
        clyde = new Clyde();

        MinecraftServer.getPacketListenerManager().setPlayListener(ClientSteerVehiclePacket.class, (clientSteerVehiclePacket, player) -> {
            if (player != this.player) return;

            final byte flags = clientSteerVehiclePacket.flags();

            if (VehicleMovementHaccUtil.jumping(flags) && isWalkablePacman(Direction.NORTH)) {
                pacman.direction = Direction.NORTH;
            } else if (VehicleMovementHaccUtil.sneaking(flags) && isWalkablePacman(Direction.SOUTH)) {
                pacman.direction = Direction.SOUTH;
            }
        });

        MinecraftServer.getPacketListenerManager().setPlayListener(ClientSteerBoatPacket.class, (clientSteerBoatPacket, player) -> {
            if (clientSteerBoatPacket.rightPaddleTurning() && isWalkablePacman(Direction.WEST)) { // turn left coz inverted
                pacman.direction = Direction.WEST;
            } else if (clientSteerBoatPacket.leftPaddleTurning() && isWalkablePacman(Direction.EAST)) {
                pacman.direction = Direction.EAST;
            }
        });
    }

    public boolean isWalkablePacman(Direction direction) {
        return isWalkable(direction, new Pos(pacman.realX, pacman.realY, 0));
    }

    public boolean isWalkableGhosts(Direction direction, Pos origin) {
        return isWalkable(direction, origin, '■', 'x');
    }

    public boolean isWalkable(Direction direction, Pos origin) {
        return isWalkable(direction, origin, '■');
    }

    public boolean isWalkable(Direction direction, Pos origin, char ... chars) {
        int newX = (int) floor(origin.x() + direction.x);
        int newY = (int) floor(origin.y() + direction.y);

        char tile = MAP_LAYOUT_LINES.get(newY).charAt(newX);

        for (char c : chars) {
            if (c == tile) return false;
        }

        return true;
    }

    private void loseCallback() {
        player.sendMessage(Component.text("Lost"));
    }

    private void moveGhosts() {
        blinky.move();
        pinky.move();
        inky.move();
        clyde.move();
    }

    private void mainLoop() {
        if (isWalkablePacman(pacman.direction)) {
            int newX = (int) floor(pacman.realX + pacman.direction.x);
            int newY = (int) floor(pacman.realY + pacman.direction.y);

            pacman.realX = newX;
            pacman.realY = newY;
            pacman.updatePosition();
        }

        moveGhosts();
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
            super(new Pos(12, 16, 0), "Blinky", new Pos(3, 35, 0), Material.RED_CONCRETE);
        }

        @Override
        protected Pos moveChase() {
            return pacman.getPos();
        }
    }

    class Pinky extends Ghost {
        public Pinky() {
            super(new Pos(13, 16, 0), "Pinky", new Pos(27, 35, 0), Material.PINK_CONCRETE);
        }

        @Override
        protected Pos moveChase() {
            return null;
        }
    }

    class Inky extends Ghost {
        public Inky() {
            super(new Pos(14, 16, 0), "Inky", new Pos(xMax, -2, 0), Material.CYAN_CONCRETE);
        }

        @Override
        protected Pos moveChase() {
            return null;
        }
    }

    class Clyde extends Ghost {
        public Clyde() {
            super(new Pos(15, 16, 0), "Clyde", new Pos(0, -2, 0), Material.LIME_CONCRETE);
        }

        @Override
        protected Pos moveChase() {
            return null;
        }
    }

    abstract class Ghost extends Sprite {
        public State state = State.CHASE;
        public final Pos scatterTargetTile;
        public Pos targetTile;

        public Direction facing = Direction.NORTH;
        public Direction lastDirection = Direction.NONE;

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

        protected final void moveTowardsTargetTile() {
            Direction d = Direction.NONE;

            double minDistance = Double.MAX_VALUE;
            for (Direction direction : Direction.values()) {
                if (direction == Direction.NONE) continue;

                double distance = squaredDistanceToTargetTile(direction);

                if (distance < minDistance && isWalkableGhosts(direction, getPos()) && (
                        rotate90Deg(true, rotate90Deg(true, direction)) != lastDirection)) {
                    minDistance = distance;
                    d = direction;
                }
            }

            realX += d.x;
            realY += d.y;
            lastDirection = d;
        }

        private double squaredDistanceToTargetTile(Direction direction) {
            return new Pos(realX + direction.x, realY + direction.y, 0).distanceSquared(
                    targetTile
            );
        }

        protected abstract Pos moveChase();

        private Pos inferNewTargetTile() {
            if (state == State.SCATTER) {
                return scatterTargetTile;
            } else if (state == State.CHASE) {
                return moveChase();
            }

            throw new RuntimeException("Invalid state (???)");
        }

        public final void move() {
            Pos newTargetTile = inferNewTargetTile();

            if (newTargetTile != null) {
                targetTile = newTargetTile;
            }

            moveTowardsTargetTile();
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