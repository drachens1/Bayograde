package org.drachens.temporary.scoreboards;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.scoreboard.Sidebar;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardBuilder;

import static org.drachens.util.KyoriUtil.compBuild;

public class DefaultScoreboard extends ContinentalScoreboards {
    @Override
    protected Sidebar createSidebar(CPlayer p) {
        return new ScoreboardBuilder.builder(compBuild("Default", NamedTextColor.GOLD, TextDecoration.BOLD))
                .addLine("vote",compBuild("Do /vote <option>",NamedTextColor.GRAY))
                .addLine("vote2", compBuild("to start the game",NamedTextColor.GRAY))
                .addLine("spectator",compBuild("Do /country join <name>",NamedTextColor.GRAY))
                .addLine("spectator2", compBuild(" to join a country.",NamedTextColor.GRAY))
                .build();
    }
}
