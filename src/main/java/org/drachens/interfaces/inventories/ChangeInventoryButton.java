package org.drachens.interfaces.inventories;

import net.minestom.server.item.ItemStack;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;

public class ChangeInventoryButton extends HotbarItemButton {
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;
    private final InventoryEnum inventoryEnum;

    public ChangeInventoryButton(ItemStack item, InventoryEnum inventoryEnum) {
        super(item);
        this.inventoryEnum = inventoryEnum;
    }

    @Override
    public void onRightClick(OnUse onUse) {
        inventoryManager.assignInventory(onUse.player(), inventoryEnum);
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        inventoryManager.assignInventory(onUse.player(), inventoryEnum);
    }
}
