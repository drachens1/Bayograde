package org.drachens.miniGameSystem.minigames;

import dev.ng5m.Util;
import dev.ng5m.util.Direction;
import dev.ng5m.util.MiniGameUtil;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientSteerBoatPacket;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.World;
import org.drachens.miniGameSystem.DynamicPixel;
import org.drachens.miniGameSystem.MiniGame;
import org.drachens.miniGameSystem.RelativePos;
import org.drachens.miniGameSystem.Sprite;
import org.drachens.player_types.CPlayer;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.*;

public final class Pacman extends MiniGame<Pacman.PacmanWorld> {
    private static final String MAP_LAYOUT = """
            ■■■■■■■■■■■■■■■■■■■■■■■■■■■■
            ■            ■■            ■
            ■ ■■■■ ■■■■■ ■■ ■■■■■ ■■■■ ■
            ■ ■  ■ ■   ■ ■■ ■   ■ ■  ■ ■
            ■ ■■■■ ■■■■■ ■■ ■■■■■ ■■■■ ■
            ■                          ■
            ■ ■■■■ ■■ ■■■■■■■■ ■■ ■■■■ ■
            ■ ■■■■ ■■ ■■■■■■■■ ■■ ■■■■ ■
            ■      ■■    ■■    ■■      ■
            ■■■■■■ ■■■■■ ■■ ■■■■■ ■■■■■■
            xxxxx■ ■■■■■ ■■ ■■■■■ ■xxxxx
            xxxxx■ ■■          ■■ ■xxxxx
            xxxxx■ ■■ ■■■.x■■■ ■■ ■xxxxx
            ■■■■■■ ■■ ■xx.xxx■ ■■ ■■■■■■
                      ■xx..xx■         \s
            ■■■■■■ ■■ ■xxxxxx■ ■■ ■■■■■■
            xxxxx■ ■■ ■■■■■■■■ ■■ ■xxxxx
            xxxxx■ ■■  READY!  ■■ ■xxxxx
            xxxxx■ ■■ ■■■■■■■■ ■■ ■xxxxx
            ■■■■■■ ■■ ■■■■■■■■ ■■ ■■■■■■
            ■            ■■            ■
            ■ ■■■■ ■■■■■ ■■ ■■■■■ ■■■■ ■
            ■ ■■■■ ■■■■■ ■■ ■■■■■ ■■■■ ■
            ■   ■■                ■■   ■
            ■■■ ■■ ■■ ■■■■■■■■ ■■ ■■ ■■■
            ■■■ ■■ ■■ ■■■■■■■■ ■■ ■■ ■■■
            ■      ■■    ■■    ■■      ■
            ■ ■■■■■■■■■■ ■■ ■■■■■■■■■■ ■
            ■ ■■■■■■■■■■ ■■ ■■■■■■■■■■ ■
            ■                          ■
            ■■■■■■■■■■■■■■■■■■■■■■■■■■■■""";

    // REVERSED ⚠
    private static final List<String> MAP_LAYOUT_LINES = Arrays.asList(MAP_LAYOUT.split("\n")).reversed();
    private static final int xMax = 28;
    private static final int yMax = 31;
    public final int fps = 2;
    private final PacmanSprite pacman;
    private final Blinky blinky;
    private final Pinky pinky;
    private final Inky inky;
    private final Clyde clyde;
    private final CPlayer player;
    public int score = 0;
    private int frames = 0;
    private Ghost.State state = Ghost.State.SCATTER;


    public Pacman(CPlayer p) {
        super(p, xMax, yMax, Material.BLACK_CONCRETE, new PacmanWorld());
        this.player = p;

        getWorld().setInstance(this);

        new Sprite.Builder()
                .setIdentifier("pacmanMap")
                .setLayout(
                        MAP_LAYOUT
                )
                .setIngredient('■', Material.BLACK_CONCRETE)
                .setIngredient(' ', Material.BLACK_CONCRETE)
                .build(new Pos(27, 30, -2), getMonitor());

        pacman = new PacmanSprite();

        blinky = new Blinky();
        pinky = new Pinky();
        inky = new Inky();
        clyde = new Clyde();

//        MinecraftServer.getPacketListenerManager().setPlayListener(ClientSteerVehiclePacket.class, (clientSteerVehiclePacket, player) -> {
//            if (player != this.player) return;
//
//            final byte flags = clientSteerVehiclePacket.flags();
//
//            if (VehicleMovementHaccUtil.jumping(flags) && isWalkablePacman(Direction.NORTH)) {
//                pacman.direction = Direction.NORTH;
//            } else if (VehicleMovementHaccUtil.sneaking(flags) && isWalkablePacman(Direction.SOUTH)) {
//                pacman.direction = Direction.SOUTH;
//            }
//        });

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

    public char getTileAt(int x, int y) {
        return MAP_LAYOUT_LINES.get(y).charAt(x);
    }

    public boolean isWalkable(Direction direction, Pos origin, char... chars) {
        int newX = (int) floor(origin.x() + direction.x);
        int newY = (int) floor(origin.y() + direction.y);

        char tile = getTileAt(newX, newY);

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

    private void updateStateFromTimePassed() {
        int seconds = frames / fps;

        if (seconds > 7 && seconds <= 27) {
            state = Ghost.State.CHASE;
        }

        if (seconds > 27 && seconds <= 34) {
            state = Ghost.State.SCATTER;
        }

        if (seconds > 34 && seconds <= 54) {
            state = Ghost.State.CHASE;
        }

        if (seconds > 54 && seconds <= 59) {
            state = Ghost.State.SCATTER;
        }

        if (seconds > 59 && seconds <= 79) {
            state = Ghost.State.CHASE;
        }

        if (seconds > 79 && seconds <= 84) {
            state = Ghost.State.SCATTER;
        }

        if (seconds > 84) {
            state = Ghost.State.CHASE;
        }
    }

    private void movePacman() {
        if (isWalkablePacman(pacman.direction)) {
            int newX = (int) floor(pacman.realX + pacman.direction.x);
            int newY = (int) floor(pacman.realY + pacman.direction.y);

            pacman.realX = newX;
            pacman.realY = newY;
            pacman.updatePosition();
        }

        if (pacman.realX + pacman.direction.x >= 28) {
            pacman.realX = 0;
            pacman.realY = 16;
        }

        if (pacman.realX + pacman.direction.x < 0) {
            pacman.realX = xMax - 1;
            pacman.realY = 16;
        }

        char tile = getTileAt((int) pacman.realX, (int) pacman.realY);
        Material material = getWorld().getInstance().getBlock((int) pacman.realX, (int) pacman.realY, -1).registry().material();
        if (tile == ' ' && material == Material.HEAVY_CORE) {
            score++;
            getWorld().getInstance().setBlock((int) pacman.realX, (int) pacman.realY, -1, Block.AIR);
        }
    }

    private void mainLoop() {
        frames++;

        updateStateFromTimePassed();

        movePacman();
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

    static class PacmanWorld extends World {
        private Pacman instance;

        public PacmanWorld() {
            super(MinecraftServer.getInstanceManager().createInstanceContainer(), new Pos(14, 15, -20));
        }

        public void setInstance(Pacman instance) {
            this.instance = instance;
        }

        @Override
        public void addPlayer(CPlayer p) {
            MiniGameUtil.putPlayerInBoat(p, getInstance());
            MiniGameUtil.startGameLoop(instance.fps, instance::mainLoop);

            for (int y = 0; y < MAP_LAYOUT_LINES.size(); ++y) {
                String line = MAP_LAYOUT_LINES.get(y);
                for (int x = 0; x < line.length(); ++x) {
                    char tile = line.charAt(x);
                    Block block = Block.HEAVY_CORE;

                    if (tile == '■') {
                        block = Block.BLUE_CONCRETE;
                    }

                    if (tile == 'x' || tile == '.') {
                        block = Block.BLACK_CONCRETE;
                    }
                    getInstance().setBlock(x, y, -1, block);
                }
            }
        }

        @Override
        public void removePlayer(CPlayer p) {
            ContinentalManagers.worldManager.unregisterWorld(this);
        }
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
            int yd = pacman.direction.y == 0 ? 0 : 4;
            int xd = pacman.direction.x == 0 ? 0 : 4;

            if (pacman.direction == Direction.NORTH)
                xd = 4;

            return new Pos(pacman.realX + pacman.direction.x + xd, pacman.realY + pacman.direction.y + yd, 0);
        }
    }

    class Inky extends Ghost {
        public Inky() {
            super(new Pos(14, 16, 0), "Inky", new Pos(xMax, -2, 0), Material.CYAN_CONCRETE);
        }

        private Pos getIntermediateTile() {
            int yd = pacman.direction.y == 0 ? 0 : 2;
            int xd = pacman.direction.x == 0 ? 0 : 2;
            if (pacman.direction == Direction.NORTH)
                xd = 2;

            return new Pos(pacman.realX + xd, pacman.realY + yd, 0);
        }

        @Override
        protected Pos moveChase() {
            Pos intermediate = getIntermediateTile();

            int vx = (int) (intermediate.x() - blinky.realX);
            int vy = (int) (intermediate.y() - blinky.realY);

            int tx = (int) (blinky.realX + 2 * vx);
            int ty = (int) (blinky.realY + 2 * vy);

            return new Pos(tx, ty, 0);
        }
    }

    class Clyde extends Ghost {
        public Clyde() {
            super(new Pos(15, 16, 0), "Clyde", new Pos(-1, -2, 0), Material.LIME_CONCRETE);
        }

        @Override
        protected Pos moveChase() {
            int distance = (int) (sqrt(pow(realX - pacman.realX, 2)) + pow(realY - pacman.realY, 2));

            if (distance > 8) {
                return scatterTargetTile;
            } else {
                return pacman.getPos();
            }
        }
    }

    abstract class Ghost extends Sprite {
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

                if (realX + direction.x >= 28) {
                    d = Direction.EAST;
                    break;
                }

                if (realX + direction.x < 0) {
                    d = Direction.WEST;
                    break;
                }

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
            CHASE
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
}