package org.drachens.temporary.troops.inventory;

import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.interfaces.items.HotbarItemButton;

public class TroopTraining extends HotbarItemButton {
    public TroopTraining(int modelData, ItemStack item) {
        super(modelData, item);
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
