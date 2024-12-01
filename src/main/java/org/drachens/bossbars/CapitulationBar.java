package org.drachens.bossbars;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.format.NamedTextColor;
import org.drachens.interfaces.HideableBossBar;

import static org.drachens.util.KyoriUtil.compBuild;

public class CapitulationBar extends HideableBossBar {
    private final BossBar capBar = getBossBar();
    public CapitulationBar() {
        super(BossBar.bossBar(compBuild("Capitulation", NamedTextColor.GOLD), 0, BossBar.Color.RED, BossBar.Overlay.NOTCHED_6));
        hide();
    }
    public void setProgress(double progress){
        capBar.progress(bound(progress));
        if (progress==1){
            hide();
        }else show();
        System.out.println(progress);
    }
    public float bound(double d){
        if (d>1)return 1;
        if (d<0)return 0;
        return (float) d;
    }
}
