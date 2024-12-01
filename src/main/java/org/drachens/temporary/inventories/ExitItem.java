package org.drachens.temporary.inventories;

import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.enums.InventoryEnum;
import org.drachens.interfaces.items.HotbarItemButton;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ExitItem extends HotbarItemButton {
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;
    public ExitItem() {
        super(10, itemBuilder(Material.BARRIER,10));
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {
        inventoryManager.assignInventory(e.getPlayer(), InventoryEnum.defaultInv);
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        inventoryManager.assignInventory(e.getPlayer(),InventoryEnum.defaultInv);
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {

    }
}
