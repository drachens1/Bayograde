package org.drachens.Manager;

import net.minestom.server.entity.Player;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;

import java.util.HashMap;
import java.util.List;

public class ScoreboardManager {
    private List<ContinentalScoreboards> scoreboards;
    private HashMap<Player, ContinentalScoreboards> activeScoreboards = new HashMap<>();
    public void update(Player p){

    }
    public ScoreboardManager(List<ContinentalScoreboards> continentalScoreboards){
        this.scoreboards = continentalScoreboards;
    }
    public void setActiveScoreboards(Player p, ContinentalScoreboards continentalScoreboards){
        activeScoreboards.put(p,continentalScoreboards);
    }
}
