package org.drachens.temporary.scoreboards.items;

import dev.ng5m.CPlayer;
import net.minestom.server.event.player.PlayerHandAnimationEvent;
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

public class ShowDiplomacy extends HotbarItemButton {
    public ShowDiplomacy() {
        super(0, itemBuilder(Material.BOOK, 0));
    }

    ScoreboardManager scoreboardManager = ContinentalManagers.scoreboardManager;
    List<CPlayer> active = new ArrayList<>();

    @Override
    public void onUse(PlayerUseItemEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        ContinentalScoreboards continentalScoreboards = scoreboardManager.getScoreboard(p);
        if (!(continentalScoreboards instanceof DefaultCountryScoreboard defaultCountryScoreboard)) return;
        if (active.contains(p)) {
            defaultCountryScoreboard.closeDiplomacy();
            active.remove(p);
        } else {
            defaultCountryScoreboard.openDiplomacy();
            active.add(p);
        }
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {

    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {

    }

    @Override
    public void onUse(PlayerHandAnimationEvent e) {

    }
}
