package org.drachens.Manager.per_instance.vote;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Task;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.interfaces.HideableBossBar;

import java.time.temporal.ChronoUnit;

import static org.drachens.util.OtherUtil.bound;

public class VoteBar extends HideableBossBar {
    private final BossBar voteBar;
    private final Instance instance;
    private Task task;

    public VoteBar(Instance instance) {
        super(BossBar.bossBar(Component.text("Vote session", NamedTextColor.WHITE), 0, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_12));
        this.voteBar = getBossBar();
        this.instance = instance;
    }

    public void start() {
        if (null != this.task) task.cancel();
        show();
        if (null != ContinentalManagers.yearManager.getYearBar(this.instance))
            ContinentalManagers.yearManager.getYearBar(instance).hide();
        task = MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
            float completion;

            @Override
            public void run() {
                completion++;
                voteBar.progress(bound(1.0f, 0.0f, completion / 12.0f));
                if (12.0f <= this.completion) {
                    hide();
                    task.cancel();
                }
            }
        }).repeat(1, ChronoUnit.SECONDS).schedule();
    }
}
