package org.drachens.temporary.scoreboards.items;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.temporary.scoreboards.country.DefaultCountryScoreboard;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ShowIdeology extends HotbarItemButton {
    public ShowIdeology() {
        super(2, itemBuilder(Material.BOOK, Component.text("Show Ideology"), 2));
    }

    ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;

    @Override
    public void onUse(PlayerUseItemEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(p);
        if (!(continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard)) return;
        if (defaultCountryScoreboard.isIdeologies()) {
            defaultCountryScoreboard.closeIdeologies();
        } else {
            defaultCountryScoreboard.openIdeologies();
        }
    }
}
