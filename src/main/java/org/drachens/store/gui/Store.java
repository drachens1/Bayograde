package org.drachens.store.gui;

import org.drachens.player_types.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.store.StoreCategory;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.drachens.util.InventoryUtil.*;
import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Store extends InventoryGUI {
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, "Store");
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        int i = 0;
        for (Map.Entry<String, StoreCategory> e : ContinentalManagers.cosmeticsManager.getStoreCategories().entrySet()) {
            this.addButton(noneEdge[i], category(e.getValue()));
            i++;
        }
        outlineInventory(this, outline(itemBuilder(Material.BLUE_STAINED_GLASS_PANE, Component.text("", NamedTextColor.AQUA))));
        addExitButton(this);
        addPlayerHeadAtSlot(this, 4);
        super.decorate(player);
    }

    private InventoryButton category(StoreCategory storeCategory) {
        return new InventoryButton()
                .creator(player -> storeCategory.getItem())
                .consumer(e -> {
                    Player p = e.getPlayer();
                    p.closeInventory();
                    ContinentalManagers.guiManager.openGUI(new StoreCategoryGUI(storeCategory), (CPlayer) p);
                });
    }

    private InventoryButton outline(ItemStack item) {
        return new InventoryButton()
                .creator(player -> item)
                .consumer(e -> {
                });
    }
}
