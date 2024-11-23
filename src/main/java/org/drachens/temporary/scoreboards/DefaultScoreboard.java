package org.drachens.temporary.scoreboards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardBuilder;
import org.drachens.Manager.scoreboards.ScoreboardManager;

import static org.drachens.util.KyoriUtil.compBuild;

public class DefaultScoreboard extends ContinentalScoreboards {
    private final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    public DefaultScoreboard() {
        super(new ScoreboardBuilder.builder(compBuild("Default", NamedTextColor.GOLD, TextDecoration.BOLD))
                .addLine("vote",compBuild("Do /vote <option>",NamedTextColor.GRAY))
                .addLine("vote2", compBuild("to start the game",NamedTextColor.GRAY))
                .addLine("spectator",compBuild("Do /country join <name>",NamedTextColor.GRAY))
                .addLine("spectator2", compBuild(" to join a country.",NamedTextColor.GRAY))
                .build());
    }

    @Override
    public void add(Player p) {
        scoreboardManager.setActiveScoreboard(p,this);
    }
}
