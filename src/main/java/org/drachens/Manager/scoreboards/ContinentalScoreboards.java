package org.drachens.Manager.scoreboards;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;

import java.util.List;

public abstract class ContinentalScoreboards {
    private final Sidebar sidebar;
    public ContinentalScoreboards(Sidebar sidebar){
        this.sidebar = sidebar;
    }
    public Sidebar getSidebar(){
        return sidebar;
    }
    public abstract void add(Player p);
}