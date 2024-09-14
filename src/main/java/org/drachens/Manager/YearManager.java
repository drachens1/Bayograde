package org.drachens.Manager;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.Instance;
import org.drachens.events.NewDay;

import java.time.temporal.ChronoUnit;

import static org.drachens.util.KyoriUtil.compBuild;

public class YearManager {
    private final BossBar yearBar;
    private final int startYear;
    private final long dayLength;
    private final Instance instance;
    public YearManager(int startYear, int endYear, long dayLength, Instance instance){
        this.startYear = startYear;
        this.dayLength = dayLength;
        this.instance = instance;
        yearBar = BossBar.bossBar(compBuild("",NamedTextColor.GOLD),0, BossBar.Color.RED, BossBar.Overlay.PROGRESS);
        for (Player p : instance.getPlayers()){
            p.showBossBar(yearBar);
        }
        run();
    }
    public void addPlayer(Player p){
        yearBar.addViewer(p);
    }
    public void removePlayer(Player p){
        yearBar.removeViewer(p);
    }
    public void run(){
        MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
            int day = 0;
            int month = 0;
            int year = startYear;
            final int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            @Override
            public void run() {
                yearBar.name(compBuild(day+"/"+month+"/"+year, NamedTextColor.GOLD));
                day++;
                if (day > daysInMonth[month]) {
                    day = 1;
                    month++;
                    if (month > 11) {
                        month = 0;
                        year++;
                    }
                }
                EventDispatcher.call(new NewDay(day,month,year,instance));
            }
        }).repeat(dayLength, ChronoUnit.MILLIS).schedule();
    }
}
