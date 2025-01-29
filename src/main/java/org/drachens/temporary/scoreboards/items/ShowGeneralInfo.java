package org.drachens.temporary.scoreboards.items;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.player_types.CPlayer;
import org.drachens.temporary.scoreboards.country.DefaultCountryScoreboard;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ShowGeneralInfo extends HotbarItemButton {
    ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;

    public ShowGeneralInfo() {
        super(itemBuilder(Material.BOOK, Component.text("Show General Info"), 5));
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(p);
        if (!(continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard)) return;
        if (defaultCountryScoreboard.isGeneralInfo()) {
            defaultCountryScoreboard.closeGeneralInfo();
        } else {
            defaultCountryScoreboard.openGeneralInfo();
        }
    }
}
