package org.drachens.interfaces.inventories;

import net.minestom.server.event.player.PlayerHandAnimationEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;

public abstract class HotbarItemButton {
    int modelData;
    ItemStack item;

    public HotbarItemButton(int modelData, ItemStack item) {
        this.modelData = modelData;
        this.item = item;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getModelData() {
        return modelData;
    }

    public void onUse(PlayerUseItemEvent e){

    }

    public void onUse(PlayerUseItemOnBlockEvent e){

    }

    public void onUse(PlayerStartDiggingEvent e){

    }

    public void onUse(PlayerHandAnimationEvent e){

    }
}
