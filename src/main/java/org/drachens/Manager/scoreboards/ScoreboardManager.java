package org.drachens.Manager.scoreboards;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

import java.util.HashMap;

public class ScoreboardManager {
    private final HashMap<Player, ContinentalScoreboards> activeScoreboards = new HashMap<>();

    public Sidebar getSidebar(Player p) {
        return activeScoreboards.get(p).getSidebar();
    }

    public void openScoreboard(ContinentalScoreboards continentalScoreboards,Player p){
        activeScoreboards.put(p,continentalScoreboards);
        continentalScoreboards.setup((CPlayer) p);
    }
}
