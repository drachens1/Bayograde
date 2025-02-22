package org.drachens.bossbars;

import com.google.gson.JsonElement;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.drachens.interfaces.HideableBossBar;
import org.drachens.interfaces.Saveable;

public class CapitulationBar extends HideableBossBar {
    private final BossBar capBar = getBossBar();

    public CapitulationBar() {
        super(BossBar.bossBar(Component.text("Capitulation", NamedTextColor.GOLD), 0, BossBar.Color.RED, BossBar.Overlay.NOTCHED_6));
        hide();
    }

    public void setProgress(double progress) {
        capBar.progress(bound(progress));
        if (1 == progress) {
            hide();
        } else show();
    }

    public float bound(double d) {
        if (1 < d) return 1;
        if (0 > d) return 0;
        return (float) d;
    }
}
