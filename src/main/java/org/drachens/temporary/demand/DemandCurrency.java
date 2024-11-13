package org.drachens.temporary.demand;

import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.interfaces.items.HotbarItemButton;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class DemandCurrency extends HotbarItemButton {
    public DemandCurrency() {
        super(3, itemBuilder(Material.IRON_SWORD));
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {

    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {

    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {

    }
}