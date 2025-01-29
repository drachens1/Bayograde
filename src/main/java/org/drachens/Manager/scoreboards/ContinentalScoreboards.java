package org.drachens.Manager.scoreboards;

import net.minestom.server.scoreboard.Sidebar;
import org.drachens.player_types.CPlayer;

public abstract class ContinentalScoreboards {
    private Sidebar sidebar;
    private CPlayer p;

    public void setup(CPlayer p) {
        sidebar = createSidebar(p);
        sidebar.addViewer(p);
        this.p = p;
    }

    protected abstract Sidebar createSidebar(CPlayer p);

    public Sidebar getSidebar() {
        return sidebar;
    }

    public CPlayer getPlayer() {
        return p;
    }
}