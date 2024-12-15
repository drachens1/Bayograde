package org.drachens.temporary.scoreboards.items;

import dev.ng5m.CPlayer;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.scoreboards.ContinentalScoreboards;
import org.drachens.Manager.scoreboards.ScoreboardManager;
import org.drachens.interfaces.items.HotbarItemButton;
import org.drachens.temporary.scoreboards.country.DefaultCountryScoreboard;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ShowGeneralInfo extends HotbarItemButton {
    public ShowGeneralInfo() {
        super(5, itemBuilder(Material.BOOK, 5));
    }

    ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    List<CPlayer> active = new ArrayList<>();

    @Override
    public void onUse(PlayerUseItemEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(p);
        if (!(continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard)) return;
        if (!active.contains(p)) {
            defaultCountryScoreboard.closeGeneralInfo();
            active.add(p);
        } else {
            defaultCountryScoreboard.openGeneralInfo();
            active.remove(p);
        }
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {

    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {

    }
}
