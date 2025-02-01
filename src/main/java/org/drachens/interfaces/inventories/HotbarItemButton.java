package org.drachens.interfaces.inventories;

import net.minestom.server.event.player.*;
import net.minestom.server.item.ItemStack;

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

    public void onSwapTo(PlayerChangeHeldSlotEvent e){}

    public void onSwapFrom(PlayerChangeHeldSlotEvent e){}
}
