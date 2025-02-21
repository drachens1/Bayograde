package org.drachens.interfaces.inventories;

import lombok.Getter;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.item.ItemStack;

@Getter
public abstract class HotbarItemButton {
    private final ItemStack item;

    protected HotbarItemButton(ItemStack item) {
        this.item = item;
    }

    // So player start digging event
    public void onLeftClickOnBlock(OnUse onUse){}

    public void onLeftClick(OnUse onUse){}

    // playerUseItemEvent
    public void onRightClick(OnUse onUse){}

    // playerUseItemOnBlockEvent
    public void onRightClickOnBlock(OnUse onUse){}

    public void onSwapTo(PlayerChangeHeldSlotEvent e){}

    public void onSwapFrom(PlayerChangeHeldSlotEvent e){}

    public void onMove(PlayerMoveEvent e) {}
}
