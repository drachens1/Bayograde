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

public class ShowEconomy extends HotbarItemButton {
    public ShowEconomy() {
        super(1, itemBuilder(Material.BOOK, Component.text("Show Economy"), 1));
    }

    ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    @Override
    public void onUse(PlayerUseItemEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(p);
        if (!(continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard)) return;
        if (defaultCountryScoreboard.isEconomy()) {
            defaultCountryScoreboard.closeEconomy();
        } else {
            defaultCountryScoreboard.openEconomy();
        }
    }
}
