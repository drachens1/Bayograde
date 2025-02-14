package org.drachens.temporary.scoreboards.items;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;
import org.drachens.temporary.scoreboards.DefaultCountryScoreboard;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ShowGeneralInfo extends HotbarItemButton {
    ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;

    public ShowGeneralInfo() {
        super(itemBuilder(Material.BOOK, Component.text("Show General Info"), 5));
    }

    @Override
    public void onRightClick(OnUse onUse) {
        CPlayer p = onUse.player();
        ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(p);
        if (!(continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard)) return;
        if (defaultCountryScoreboard.isGeneralInfo()) {
            defaultCountryScoreboard.closeGeneralInfo();
        } else {
            defaultCountryScoreboard.openGeneralInfo();
        }
    }
}
