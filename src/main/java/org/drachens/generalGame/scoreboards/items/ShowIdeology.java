package org.drachens.generalGame.scoreboards.items;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.generalGame.scoreboards.DefaultCountryScoreboard;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ShowIdeology extends HotbarItemButton {
    final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;

    public ShowIdeology() {
        super(itemBuilder(Material.BOOK, Component.text("Show Ideology"), 2));
    }

    @Override
    public void onRightClick(OnUse onUse) {
        CPlayer p = onUse.player();
        ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(p);
        if (!(continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard)) return;
        if (defaultCountryScoreboard.isIdeologies()) {
            defaultCountryScoreboard.closeIdeologies();
        } else {
            defaultCountryScoreboard.openIdeologies();
        }
    }
}
