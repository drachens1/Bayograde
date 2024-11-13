package org.drachens.bossbars;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Task;
import org.drachens.events.NewDay;
import org.drachens.events.System.ResetEvent;
import org.drachens.interfaces.HideableBossBar;
import org.drachens.interfaces.VotingOption;

import java.time.temporal.ChronoUnit;

import static org.drachens.util.KyoriUtil.compBuild;

public class YearBar extends HideableBossBar {
    private final BossBar yearBar = getBossBar();
    private final Instance instance;
    private Task task;

    public YearBar(Instance instance) {
        super(BossBar.bossBar(compBuild("", NamedTextColor.GOLD), 0, BossBar.Color.RED, BossBar.Overlay.NOTCHED_6));
        this.instance = instance;
        hide();
    }

    public void cancelTask() {
        if (task != null) task.cancel();
    }

    public void run(VotingOption votingOption) {
        show();
        System.out.println("Showing hopefully");
        if (task != null) task.cancel();
        task = MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
            final int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            int day = 0;
            int month = 0;
            int year = votingOption.getStartingYear();
            int endYear = votingOption.getEndYear();

            @Override
            public void run() {
                yearBar.name(compBuild(day + "/" + month + "/" + year, NamedTextColor.GOLD));
                day++;
                if (day > daysInMonth[month]) {
                    day = 1;
                    month++;
                    if (month > 11) {
                        month = 0;
                        year++;
                    }
                }
                EventDispatcher.call(new NewDay(day, month, year, instance));
                if (year >= endYear) {
                    System.out.println("Reset!");
                    EventDispatcher.call(new ResetEvent(instance));
                }
            }
        }).repeat(votingOption.getDayLength(), ChronoUnit.MILLIS).schedule();
    }
}
