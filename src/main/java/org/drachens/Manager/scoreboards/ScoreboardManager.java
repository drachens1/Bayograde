package org.drachens.Manager.scoreboards;

import org.drachens.player_types.CPlayer;
import net.minestom.server.entity.Player;

import java.util.HashMap;

public class ScoreboardManager {
    private final HashMap<Player, ContinentalScoreboards> activeScoreboards = new HashMap<>();

    public ContinentalScoreboards getScoreboard(Player p) {
        return activeScoreboards.get(p);
    }

    public void openScoreboard(ContinentalScoreboards continentalScoreboards, Player p) {
        activeScoreboards.put(p, continentalScoreboards);
        continentalScoreboards.setup((CPlayer) p);
    }
}
