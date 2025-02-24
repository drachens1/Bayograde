package org.drachens.interfaces.inventories;

import net.minestom.server.item.Material;
import org.drachens.Manager.decorational.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ExitItem extends HotbarItemButton {
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;

    public ExitItem() {
        super(itemBuilder(Material.BARRIER, 10));
    }

    @Override
    public void onRightClick(OnUse onUse) {
        inventoryManager.assignInventory(onUse.player(), ContinentalManagers.world(onUse.instance()).dataStorer().votingOption.getDefaultInventory());
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        inventoryManager.assignInventory(onUse.player(), ContinentalManagers.world(onUse.instance()).dataStorer().votingOption.getDefaultInventory());
    }
}
