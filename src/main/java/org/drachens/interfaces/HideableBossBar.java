package org.drachens.interfaces;

import net.kyori.adventure.bossbar.BossBar;
import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class HideableBossBar {
    private final BossBar bossBar;
    private final List<Player> players = new ArrayList<>();
    private boolean shown;

    protected HideableBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void addPlayer(Player p) {
        if (shown) bossBar.addViewer(p);
        players.add(p);
    }

    public void removePlayer(Player p) {
        bossBar.removeViewer(p);
        players.remove(p);
    }

    public void show() {
        if (shown) return;
        shown = true;
        for (Player p : players) {
            bossBar.addViewer(p);
        }
    }

    public void hide() {
        if (!shown) return;
        shown = false;
        for (Player p : players) {
            bossBar.removeViewer(p);
        }
    }

    public boolean isShown() {
        return shown;
    }
}
