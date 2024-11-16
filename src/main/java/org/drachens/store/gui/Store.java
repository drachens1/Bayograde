package org.drachens.store.gui;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.store.StoreCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Store extends InventoryGUI {

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW,"store");
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        int i = 0;
        for (Map.Entry<String, StoreCategory> e : ContinentalManagers.defaultsStorer.storeCategories.getStoreCategories().entrySet()){
            this.addButton(i,category(e.getValue()));
            i++;
        }
        super.decorate(player);
    }
    private InventoryButton category(StoreCategory storeCategory) {
        return new InventoryButton()
                .creator(player -> storeCategory.getItem())
                .consumer(e -> {
                    Player p = e.getPlayer();
                    p.closeInventory();
                    ContinentalManagers.guiManager.openGUI(new StoreCategoryGUI(storeCategory),(CPlayer) p);
                });
    }

}
