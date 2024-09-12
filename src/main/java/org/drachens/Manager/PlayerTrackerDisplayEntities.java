package org.drachens.Manager;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.other.TextDisplay;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.KyoriUtil.compBuild;

public class PlayerTrackerDisplayEntities {
    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player p) {
        players.add(p);
        final TextDisplay currentDisplay = new TextDisplay(
                compBuild("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", NamedTextColor.GOLD),
                calculatePositionInFrontOfPlayer(p, 1),
                p.getInstance()
        );
        currentDisplay.show(p);
        MinecraftServer.getSchedulerManager().buildTask(() -> {
            Pos newPosition = calculatePositionInFrontOfPlayer(p, 2.0);
            float yawToFacePlayer = calculateYawToFacePlayer(newPosition, p.getPosition());
            float pitchToFacePlayer = calculatePitchToFacePlayer(newPosition, p.getPosition());
            currentDisplay.move(newPosition.withYaw(yawToFacePlayer).withPitch(pitchToFacePlayer));

        }).repeat(1,ChronoUnit.MILLIS).schedule();

    }

    public void removePlayer(Player p) {
        players.remove(p);
    }

    private Pos calculatePositionInFrontOfPlayer(Player player, double distance) {
        Pos playerPos = player.getPosition();
        float yaw = player.getPosition().yaw();
        float pitch = player.getPosition().pitch();

        double yawRadians = Math.toRadians(yaw);
        double pitchRadians = Math.toRadians(pitch);

        double offsetX = -Math.sin(yawRadians) * Math.cos(pitchRadians) * distance;
        double offsetY = -Math.sin(pitchRadians) * distance;
        double offsetZ = Math.cos(yawRadians) * Math.cos(pitchRadians) * distance;

        return playerPos.add(offsetX, offsetY + 1.5, offsetZ);
    }

    private float calculateYawToFacePlayer(Pos from, Pos to) {
        double dx = to.x() - from.x();
        double dz = to.z() - from.z();

        return (float) Math.toDegrees(Math.atan2(-dx, dz));
    }
    private float calculatePitchToFacePlayer(Pos from, Pos to) {
        double dx = to.x() - from.x();
        double dz = to.z() - from.z();
        double dy = to.y() - from.y();

        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        return (float) Math.toDegrees(Math.atan2(dy, distanceXZ));
    }
}
