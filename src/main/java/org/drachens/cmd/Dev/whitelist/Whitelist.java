package org.drachens.cmd.Dev.whitelist;

import java.util.List;
import java.util.UUID;

public class Whitelist {
    private boolean whitelist;
    private final List<UUID> players;
    public Whitelist(List<UUID> players, boolean whitelist){
        this.players = players;
        this.whitelist = whitelist;
    }

    public void addPlayer(UUID p){
        players.add(p);
    }

    public void removePlayer(UUID p){
        players.remove(p);
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public boolean active(){
        return whitelist;
    }

    public void setActive(boolean active){
        whitelist = active;
    }
}
