package org.drachens.Manager.scoreboards;

import net.minestom.server.entity.Player;

import java.util.HashMap;

public class ScoreboardManager {
    private HashMap<String,ContinentalScoreboards> scoreboards = new HashMap<>();
    private final HashMap<Player, ContinentalScoreboards> activeScoreboards = new HashMap<>();
    public void update(Player p){
        activeScoreboards.get(p).update(p);
    }
    public void setActiveScoreboard(Player p, ContinentalScoreboards continentalScoreboards){
        activeScoreboards.put(p,continentalScoreboards);
    }
    public void addScoreboard(String name, ContinentalScoreboards continentalScoreboards){
        scoreboards.put(name,continentalScoreboards);
    }
    public void setScoreboards(HashMap<String,ContinentalScoreboards> scoreboards){
        this.scoreboards = scoreboards;
    }
    public HashMap<String,ContinentalScoreboards> getScoreboards(){
        return scoreboards;
    }
    public ContinentalScoreboards getScoreboard(String name){
        return scoreboards.get(name);
    }
}
