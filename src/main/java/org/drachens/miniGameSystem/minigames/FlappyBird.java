package org.drachens.miniGameSystem.minigames;

import com.google.gson.*;
import dev.ng5m.CPlayer;
import dev.ng5m.Util;
import dev.ng5m.events.EventHandlerProvider;
import dev.ng5m.util.MiniGameUtil;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientSteerVehiclePacket;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.World;
import org.drachens.interfaces.HideableBossBar;
import org.drachens.miniGameSystem.MiniGame;
import org.drachens.miniGameSystem.Sprite;

import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.floor;
import static java.lang.Math.scalb;

public class FlappyBird extends MiniGame
        implements EventHandlerProvider {
    public static final File db = new File("flappy.json");

    private final Sprite bird;
    private static final double gravity = 0.5;
    private double p = 0;
    private final double realX;

    private final int xMax;
    private final int yMax;

    private boolean gameEnded = false;
    private boolean gameStarted = false;

    private static final String SID_BIRD = "bird";
    private static final String SID_PIPE = "flappyPipe";

    private final FlappyBar flappyBar;

    private final List<PipeSprite> pipes = new ArrayList<>();

    private final CPlayer player;

    private int score = 0;

    public FlappyBird(CPlayer p, int xMax, int yMax) {
        super(p, xMax, yMax, Material.BLUE_CONCRETE, new FlappyBird.FlappyWorld(), new Pos(xMax / 2d, yMax / 2d, -(xMax / 2d)));
        this.xMax = xMax;
        this.yMax = yMax;
        this.player = p;

        flappyBar = new FlappyBar();

        ((FlappyWorld) getWorld()).setInstance(this);

        MinecraftServer.getPacketListenerManager().setPlayListener(ClientSteerVehiclePacket.class, (clientSteerVehiclePacket, player) -> {
            if (player != this.player) return;
            if ((clientSteerVehiclePacket.flags() & 0x01) == 0) return;

            this.p += gravity * 2d;
        });

        this.p = yMax / 2d - 1d;
        this.realX = xMax / 2d;

        pipePair();

        bird = new Sprite.Builder().setLayout(
                        " Y\n"
                                + "YYY"
                ).setIngredient('Y', Material.YELLOW_CONCRETE)
                .setCollisionFunction((collided) -> {
                    if (!collided.getIdentifier().equals(SID_PIPE)) return;

                    loseCallback();
                })
                .setIdentifier(SID_BIRD)
                .build(new Pos(this.realX, this.p, 1), getMonitor());

        gameStarted = true;
    }

    private void pipePair() {
        int pipeHeight = ThreadLocalRandom.current().nextInt(10) + (score / 20);

        pipeSprite(true, pipeHeight, new Pos(realX + 10, yMax, 0));
        pipeSprite(false, pipeHeight, new Pos(realX + 10, yMax / 2d - pipeHeight, 0));
    }

    private void pipeSprite(boolean down, int height, Pos pos) {
        pipes.add(new PipeSprite(height, down, pos));
    }

    private JsonObject getDBRoot() {
        return JsonParser.parseString(Util.readFile(db.toPath())).getAsJsonObject();
    }

    private String getLeaderboard() {
        JsonObject root = getDBRoot();

        HashMap<String, Integer> nameToScoreMap = new HashMap<>();

        for (String key : root.keySet()) {
            int score = root.get(key).getAsInt();

            nameToScoreMap.put(key, score);
        }

        List<Map.Entry<String, Integer>> entryList = new ArrayList<>(nameToScoreMap.entrySet());
        entryList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        LinkedHashMap<String, Integer> sortedScores = new LinkedHashMap<>();

        for (Map.Entry<String, Integer> entry : entryList) {
            sortedScores.put(entry.getKey(), entry.getValue());
        }

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : sortedScores.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
        }

        return sb.toString();
    }

    private void updateDB(String playerName, int score) {
        JsonObject root = getDBRoot();

        if (root.has(playerName)) {
            root.remove(playerName);
        }

        root.add(playerName, new JsonPrimitive(score));

        Util.writeString(db, root.toString());
    }

    private void loseCallback() {
        if (gameEnded || !gameStarted) return;
        gameEnded = true;

        updateDB(player.getUsername(), score);

        player.sendMessage(Component.text("You lose! Score: %d".formatted(score)));
        player.sendMessage(Component.text(getLeaderboard()));
        player.setInstance(ContinentalManagers.worldManager.getDefaultWorld().getInstance());
    }

    class PipeSprite extends Sprite {
        public double realX = 0;

        public PipeSprite(int height, boolean down, Pos pos) {
            super(pos, FlappyBird.this.getMonitor(), SID_PIPE, null);

            Sprite.Builder.loadLayout(1,
                    down
                            ? (" ###\n".repeat(height) + "#####")
                            : ("#####\n" + " ###\n".repeat(height)), Map.of('#', Material.GREEN_CONCRETE), this);
        }
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
            flappyBird.flappyBar.addPlayer(p);
            

            MiniGameUtil.startGameLoop(flappyBird, 60, () -> {
                flappyBird.pipes.forEach(pipeSprite -> {
                    pipeSprite.realX += 0.2 + (flappyBird.score / 200d);
                    pipeSprite.delete();
                    pipeSprite.setPos(pipeSprite.getPos().withX(floor(pipeSprite.realX)));
                });

                if (flappyBird.pipes.getLast().realX >= 10) {
                    flappyBird.pipePair();
                    flappyBird.score++;
                    flappyBird.flappyBar.setScore(flappyBird.score);
                }

                flappyBird.p -= gravity;

                flappyBird.bird.setPos(new Pos(floor(flappyBird.realX), floor(flappyBird.p), 0));
            });
        }

        @Override
        public void removePlayer(CPlayer p) {
            flappyBird.flappyBar.removePlayer(p);
        }
    }

    static class FlappyBar {
        private final BossBar bossBar;

        public FlappyBar() {
            bossBar = BossBar.bossBar(Component.text(),1f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);
        }

        public void addPlayer(Player p){
            bossBar.addViewer(p);
        }

        public void removePlayer(Player p){
            bossBar.removeViewer(p);
        }

        public void setScore(int score){
            bossBar.name(Component.text()
                            .append(Component.text("Score: "))
                            .append(Component.text(score))
                    .build());
        }
    }
}
