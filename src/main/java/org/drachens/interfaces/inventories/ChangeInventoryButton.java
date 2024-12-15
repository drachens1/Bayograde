package org.drachens.interfaces.inventories;

import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.enums.InventoryEnum;
import org.drachens.interfaces.items.HotbarItemButton;

public class ChangeInventoryButton extends HotbarItemButton {
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;
    private final InventoryEnum inventoryEnum;

    public ChangeInventoryButton(int modelData, ItemStack item, InventoryEnum inventoryEnum) {
        super(modelData, item);
        this.inventoryEnum = inventoryEnum;
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {
        inventoryManager.assignInventory(e.getPlayer(), inventoryEnum);
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        inventoryManager.assignInventory(e.getPlayer(), inventoryEnum);
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {

    }
}
