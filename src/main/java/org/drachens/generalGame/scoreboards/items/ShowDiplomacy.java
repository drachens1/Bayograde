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

public class ShowDiplomacy extends HotbarItemButton {
    final ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;

    public ShowDiplomacy() {
        super(itemBuilder(Material.BOOK, Component.text("Show Diplomacy"), 0));
    }

    @Override
    public void onRightClick(OnUse onUse) {
        CPlayer p = onUse.player();
        ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(p);
        if (!(continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard)) return;
        if (defaultCountryScoreboard.isDiplomacy()) {
            defaultCountryScoreboard.closeDiplomacy();
        } else {
            defaultCountryScoreboard.openDiplomacy();
        }
    }
}
