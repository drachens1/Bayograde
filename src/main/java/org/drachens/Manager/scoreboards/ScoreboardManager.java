package org.drachens.Manager.scoreboards;

import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

import java.util.HashMap;

public class ScoreboardManager {
    private final HashMap<Player, ContinentalScoreboards> activeScoreboards = new HashMap<>();
    private HashMap<String, ContinentalScoreboards> scoreboards = new HashMap<>();

    public Sidebar getSidebar(Player p) {
        return activeScoreboards.get(p).getSidebar();
    }

    public void setActiveScoreboard(Player p, ContinentalScoreboards continentalScoreboards) {
        activeScoreboards.put(p, continentalScoreboards);
        continentalScoreboards.getSidebar().addViewer(p);
    }

    public void addScoreboard(String name, ContinentalScoreboards continentalScoreboards) {
        scoreboards.put(name, continentalScoreboards);
    }

    public void setScoreboards(HashMap<String, ContinentalScoreboards> scoreboards) {
        this.scoreboards = scoreboards;
    }

    public ContinentalScoreboards getScoreboard(String name) {
        return scoreboards.get(name);
    }
}
