package org.drachens.miniGameSystem.minigames;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.ng5m.Util;
import dev.ng5m.util.MiniGameUtil;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientSteerBoatPacket;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.World;
import org.drachens.miniGameSystem.MiniGame;
import org.drachens.miniGameSystem.MiniGameRunnable;
import org.drachens.miniGameSystem.Sprite;
import org.drachens.miniGameSystem.minigames.FlappyBird.FlappyWorld;
import org.drachens.player_types.CPlayer;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.floor;

public class FlappyBird extends MiniGame<FlappyWorld> {
    public static final File db = new File("flappy.json");
    private static final double gravity = 0.5;
    private static final String SID_BIRD = "bird";
    private static final String SID_PIPE = "flappyPipe";
    private final Sprite bird;
    private final double realX;
    private final int yMax;
    private final FlappyBar flappyBar;
    private final List<PipeSprite> pipes = new ArrayList<>();
    private final CPlayer player;
    private double pos;
    private boolean gameEnded;
    private final boolean gameStarted;
    private int score;

    public FlappyBird(CPlayer p, int xMax, int yMax) {
        super(p, xMax, yMax, Material.BLUE_CONCRETE, new FlappyWorld(xMax, yMax));
        this.yMax = yMax;
        this.player = p;

        flappyBar = new FlappyBar();

        getWorld().setInstance(this);

        MinecraftServer.getPacketListenerManager().setPlayListener(ClientSteerBoatPacket.class, (clientSteerVehiclePacket, player) -> {
            if (player != this.player) return;
            if (clientSteerVehiclePacket.leftPaddleTurning() || clientSteerVehiclePacket.rightPaddleTurning())
                this.pos += gravity * 2.0d;
        });

        this.pos = yMax / 2.0d - 1.0d;
        this.realX = xMax / 2.0d;

        pipePair();

        bird = new Sprite.Builder().setLayout(
                        " Y\n"
                                + "YYY"
                ).setIngredient('Y', Material.YELLOW_CONCRETE)
                .setCollisionFunction(collided -> {
                    if (!collided.getIdentifier().equals(SID_PIPE)) return;

                    loseCallback();
                })
                .setIdentifier(SID_BIRD)
                .build(new Pos(this.realX, this.pos, 1), getMonitor());

        gameStarted = true;
    }

    private void pipePair() {
        int pipeHeight = new Random(new Random().nextLong()).nextInt(10) + (score / 20);

        pipeSprite(true, pipeHeight, new Pos(realX + 10, yMax, 0));
        pipeSprite(false, pipeHeight, new Pos(realX + 10, this.yMax / 2.0d - pipeHeight, 0));
    }

    private void pipeSprite(boolean down, int height, Pos pos) {
        pipes.add(new PipeSprite(height, down, pos));
    }

    private JsonObject getDBRoot() {
        return JsonParser.parseString(Util.readFile(db.toPath())).getAsJsonObject();
    }

    private String getLeaderboard() {
        JsonObject root = getDBRoot();

        return root.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(entry -> entry.getValue().getAsInt()))
                .map(entry -> entry.getKey() + ": " + entry.getValue().getAsInt())
                .collect(Collectors.joining("\n"));
    }

    private void updateDB(String playerName, int score) {
        JsonObject root = getDBRoot();

        if (root.has(playerName)) {
            root.remove(playerName);
        }

        root.add(playerName, new JsonPrimitive(score));

        Util.writeString(db, root.toString());
    }

    private int getDBScore(String player) {
        JsonObject root = getDBRoot();

        if (root.has(player)) {
            return root.get(player).getAsInt();
        }
        return -1;
    }

    private void loseCallback() {
        if (gameEnded || !gameStarted) return;
        gameEnded = true;

        int dbScore = getDBScore(player.getUsername());

        if (score > dbScore) {
            updateDB(player.getUsername(), score);
        }

        player.sendMessage(Component.text("You lose! Score: %d".formatted(score)));
        player.sendMessage(Component.text(getLeaderboard()));
        player.setInstance(ContinentalManagers.worldManager.getDefaultWorld().getInstance());
    }

    private void mainLoop() {
        pipes.forEach(pipeSprite -> {
            pipeSprite.realX += 0.2 + (score / 100.0d);
            pipeSprite.delete();
            pipeSprite.setPos(pipeSprite.getPos().withX(floor(pipeSprite.realX)));
        });

        double scoreDiv = score / 100.0d;
        double gap = 15 - (
                3 < scoreDiv ? scoreDiv : 3
        );

        if (pipes.getLast().realX >= gap) {
            pipePair();
            score++;
            flappyBar.setScore(score);
        }

        pos -= gravity;
        bird.setPos(new Pos(floor(realX), floor(pos), 0));
    }

    static class FlappyWorld extends World {
        private FlappyBird flappyBird;

        public FlappyWorld(double xMax, double yMax) {
            super(MinecraftServer.getInstanceManager().createInstanceContainer(), new Pos(xMax / 2.0d, yMax / 2.0d, -(xMax / 2.0d)));
        }

        public void setInstance(FlappyBird flappyBird) {
            this.flappyBird = flappyBird;
        }

        @Override
        public void addPlayer(CPlayer p) {
            flappyBird.flappyBar.addPlayer(p);

            MiniGameUtil.putPlayerInBoat(p, getInstance());
            MiniGameUtil.startGameLoop(60, () -> flappyBird.mainLoop());
        }

        @Override
        public void removePlayer(CPlayer p) {
            flappyBird.flappyBar.removePlayer(p);
            ContinentalManagers.worldManager.unregisterWorld(this);
        }

        @Override
        public void playerMove(PlayerMoveEvent e) {
            e.setCancelled(true);
        }
    }

    static class FlappyBar {
        private final BossBar bossBar;

        public FlappyBar() {
            bossBar = BossBar.bossBar(Component.text(), 1.0f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        }

        public void addPlayer(Player p) {
            bossBar.addViewer(p);
        }

        public void removePlayer(Player p) {
            bossBar.removeViewer(p);
        }

        public void setScore(int score) {
            bossBar.name(Component.text()
                    .append(Component.text("Score: "))
                    .append(Component.text(score))
                    .build());
        }
    }

    class PipeSprite extends Sprite {
        public double realX;

        public PipeSprite(int height, boolean down, Pos pos) {
            super(pos, FlappyBird.this.getMonitor(), SID_PIPE, (MiniGameRunnable) null);

            Sprite.Builder.loadLayout(1,
                    down
                            ? (" ###\n".repeat(height) + "#####")
                            : ("#####\n" + " ###\n".repeat(height)), Map.of('#', Material.GREEN_CONCRETE), this);
        }
    }
}
