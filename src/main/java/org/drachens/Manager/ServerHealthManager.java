package org.drachens.Manager;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.server.ServerTickMonitorEvent;

import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ServerHealthManager {
    private final LocalTime startupTime = LocalTime.now();
    private final List<TPSData> tpsHistory = new ArrayList<>();
    private long lastTickTime = System.nanoTime();
    private double tps = 20;

    public ServerHealthManager() {

        MinecraftServer.getGlobalEventHandler().addListener(ServerTickMonitorEvent.class, e -> {
            long currentTime = System.nanoTime();
            long tickDuration = currentTime - lastTickTime;
            lastTickTime = currentTime;

            tps = calculateTPS(tickDuration);
        });

        MinecraftServer.getSchedulerManager().buildTask(() -> {
            logTPS(tps);
        }).repeat(1, ChronoUnit.SECONDS).schedule();
    }

    private double calculateTPS(long tickDuration) {
        if (tickDuration <= 0) return 25.0;
        return (1_000_000_000.0 / tickDuration);
    }

    private void logTPS(double tps) {
        long currentTime = System.currentTimeMillis();
        tpsHistory.add(new TPSData(tps, currentTime));

        if (tpsHistory.size() > 3000) {
            tpsHistory.removeFirst();
        }
    }

    public double getTps() {
        return tps;
    }

    public Double getTpsFromSecondsAgo(int seconds) {
        long targetTime = System.currentTimeMillis() - (seconds * 1000L);
        for (int i = tpsHistory.size() - 1; i >= 0; i--) {
            TPSData data = tpsHistory.get(i);
            if (data.timestamp <= targetTime) {
                return data.tps;
            }
        }
        return null;
    }

    public Long getUptime() {
        return Duration.between(startupTime, LocalTime.now()).toSeconds();
    }

    private record TPSData(double tps, long timestamp) {
    }
}
