package org.drachens.Manager.scoreboards.temp;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.scoreboard.Sidebar;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;

import static org.drachens.util.KyoriUtil.compBuild;

public class DefaultScoreboard implements ContinentalScoreboards {
    private final Sidebar sb;
    private final ScoreboardManager scoreboardManager;

    public DefaultScoreboard(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
        sb = new Sidebar(compBuild("Spectator", NamedTextColor.GRAY, TextDecoration.BOLD));
        sb.createLine(new Sidebar.ScoreboardLine("top", compBuild("Shift click a country to join one", NamedTextColor.GRAY), 0));
    }

    @Override
    public void update(Player p) {

    }

    @Override
    public void add(Player p) {
        scoreboardManager.setActiveScoreboard(p, this);
        sb.addViewer(p);
    }
}