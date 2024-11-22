package org.drachens.dataClasses;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.timer.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Delay {
    private final List<Player> cd = new ArrayList<>();
    private final Long length;
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    public Delay(Long length){
        this.length = length;
    }
    public boolean hasCooldown(Player p){
        return cd.contains(p);
    }
    public void startCooldown(Player p){
        cd.add(p);
        scheduler.buildTask(() -> cd.remove(p)).delay(length, TimeUnit.MILLISECONDS.toChronoUnit()).schedule();
    }
}
