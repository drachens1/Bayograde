package org.drachens.interfaces.inventories;

import net.minestom.server.item.ItemStack;
import org.drachens.Manager.decorational.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;

public class ChangeInventoryButton extends HotbarItemButton {
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;
    private final HotbarInventory inventoryEnum;

    public ChangeInventoryButton(ItemStack item, InventoryEnum inventoryEnum) {
        super(item);
        this.inventoryEnum = inventoryEnum.getHotbarInventory();
    }

    public ChangeInventoryButton(ItemStack item, HotbarInventory hotbarInventory) {
        super(item);
        this.inventoryEnum = hotbarInventory;
    }

    @Override
    public void onRightClick(OnUse onUse) {
        inventoryManager.changeInventory(onUse.player(), inventoryEnum);
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        inventoryManager.changeInventory(onUse.player(), inventoryEnum);
    }
}
