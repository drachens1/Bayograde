package dev.ng5m.util;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityPose;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import java.time.Duration;

public class MiniGameUtil {

    public static void startGameLoop(int fps, Runnable task) {
        Preconditions.assertTrue(fps > 0, "FPS must be a positive non-zero value");

        MinecraftServer.getSchedulerManager().buildTask(task).repeat(Duration.ofMillis(1000 / fps)).schedule();
    }

    public static void putPlayerInBoat(Player player, Instance instance) {
        Entity entity = new Entity(EntityType.OAK_BOAT);
        entity.setInvisible(true);
        entity.setInstance(instance, player.getPosition());
        entity.addPassenger(player);
        player.setPose(EntityPose.STANDING);
    }

}
