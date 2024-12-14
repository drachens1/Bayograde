package dev.ng5m.util;

import net.minestom.server.MinecraftServer;
import org.drachens.miniGameSystem.MiniGame;

import java.time.Duration;

public class MiniGameUtil {

    public static <T extends MiniGame> void startGameLoop(T miniGame, int fps, Runnable task) {
        Preconditions.assertTrue(fps > 0, "FPS must be a positive non-zero value");

        MinecraftServer.getSchedulerManager().buildTask(task).repeat(Duration.ofMillis(1000 / fps)).schedule();
    }

}
