package org.drachens.interfaces.inventories;

import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.events.other.PlayerChangeActiveItemEvent;

public abstract class HotbarItemButton {
    private final ItemStack item;

    public HotbarItemButton(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public void onUse(PlayerUseItemEvent e) {}

    public void onUse(PlayerUseItemOnBlockEvent e) {}

    public void onUse(PlayerStartDiggingEvent e) {}

    public void onUse(PlayerHandAnimationEvent e) {}

    public void onSwapTo(PlayerChangeActiveItemEvent e){}

    public void onSwapFrom(PlayerChangeActiveItemEvent e){}
}
