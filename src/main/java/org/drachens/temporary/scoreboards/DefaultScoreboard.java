package org.drachens.temporary.scoreboards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.scoreboard.Sidebar;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardBuilder;
import org.drachens.player_types.CPlayer;

public class DefaultScoreboard extends ContinentalScoreboards {
    private final Sidebar scoreboard = new ScoreboardBuilder(Component.text("Default", NamedTextColor.GOLD, TextDecoration.BOLD))
            .addLine("vote", Component.text("Do /vote <option>", NamedTextColor.GRAY))
            .addLine("vote2", Component.text("to start the game", NamedTextColor.GRAY))
            .addLine("spectator", Component.text("Do /country join <name>", NamedTextColor.GRAY))
            .addLine("spectator2", Component.text(" to join a country.", NamedTextColor.GRAY))
            .build();

    @Override
    protected Sidebar createSidebar(CPlayer p) {
        return scoreboard;
    }
}
