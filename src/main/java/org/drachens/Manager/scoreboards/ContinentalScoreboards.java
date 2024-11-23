package org.drachens.Manager.scoreboards;

import dev.ng5m.CPlayer;
import net.minestom.server.scoreboard.Sidebar;

public abstract class ContinentalScoreboards {
    private Sidebar sidebar;
    private CPlayer p;
    public void setup(CPlayer p){
        sidebar = createSidebar(p);
        sidebar.addViewer(p);
        this.p = p;
    }
    protected abstract Sidebar createSidebar(CPlayer p);
    public Sidebar getSidebar(){
        return sidebar;
    }
    public CPlayer getPlayer(){
        return p;
    }
}