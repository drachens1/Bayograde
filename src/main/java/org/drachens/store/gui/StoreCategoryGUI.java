package org.drachens.store.gui;

import dev.ng5m.CPlayer;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.store.StoreCategory;
import org.drachens.store.StoreItem;
import org.jetbrains.annotations.NotNull;

public class StoreCategoryGUI extends InventoryGUI {
    private final StoreCategory storeCategory;

    public StoreCategoryGUI(StoreCategory storeCategory){
        this.storeCategory = storeCategory;
        this.getInventory().setTitle(storeCategory.getName());
    }

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW,"");
    }

    @Override
    public void decorate(@NotNull CPlayer p) {
        int i = 0;
        for (StoreItem storeItem : storeCategory.getStoreItems()){
            addButton(i,storeItemButton(storeItem,p));
            i++;
        }
        super.decorate(p);
    }

    private InventoryButton storeItemButton(StoreItem storeItem, CPlayer p) {
        return new InventoryButton()
                .creator(player -> storeItem.getItem(p))
                .consumer(e -> {
                    if (!storeItem.canBuy(p))return;
                });
    }

}