package org.drachens.Manager.per_instance.vote;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.timer.Task;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.interfaces.HideableBossBar;

import java.time.temporal.ChronoUnit;

import static org.drachens.util.KyoriUtil.compBuild;

public class VoteBar extends HideableBossBar {
    private final BossBar voteBar;
    private Task task;
    private final Instance instance;
    public VoteBar(Instance instance){
        super(BossBar.bossBar(compBuild("Vote session", NamedTextColor.WHITE), 0, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_12));
        this.voteBar = getBossBar();
        this.instance = instance;
    }

    public void start() {
        if (task!=null)task.cancel();
        show();
        if(ContinentalManagers.yearManager.getYearBar(instance)!=null)ContinentalManagers.yearManager.getYearBar(instance).hide();
        task = MinecraftServer.getSchedulerManager().buildTask(new Runnable() {
            float completion = 0f;

            @Override
            public void run() {
                completion++;
                voteBar.progress(clamp(0f,1f,completion/12f));
                if (completion>=12f){
                    System.out.println("Vote bar hidden");
                    hide();
                    task.cancel();
                }
            }
        }).repeat(1, ChronoUnit.SECONDS).schedule();
    }
    float clamp(float min, float max, float number){
        if (number>max)number=max;
        if (number<min)number=min;
        return number;
    }
}
