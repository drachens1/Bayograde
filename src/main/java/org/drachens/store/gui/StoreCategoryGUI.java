package org.drachens.store.gui;

import org.drachens.player_types.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.store.StoreCategory;
import org.drachens.store.StoreItem;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.InventoryUtil.*;
import static org.drachens.util.ItemStackUtil.itemBuilder;

public class StoreCategoryGUI extends InventoryGUI {
    private final StoreCategory storeCategory;

    public StoreCategoryGUI(StoreCategory storeCategory) {
        this.storeCategory = storeCategory;
        this.getInventory().setTitle(storeCategory.getName());
    }

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, "");
    }

    @Override
    public void decorate(@NotNull CPlayer p) {
        int i = 0;
        for (StoreItem storeItem : storeCategory.getStoreItems()) {
            addButton(noneEdge[i], storeItemButton(storeItem, p));
            i++;
        }
        outlineInventory(this, outline(itemBuilder(Material.BLUE_STAINED_GLASS_PANE, Component.text("", NamedTextColor.AQUA))));
        addExitButton(this);
        addPlayerHeadAtSlot(this, 4);
        super.decorate(p);
    }

    private InventoryButton storeItemButton(StoreItem storeItem, CPlayer p) {
        return new InventoryButton()
                .creator(player -> storeItem.getItem(p))
                .consumer(e -> {});
    }

    private InventoryButton outline(ItemStack item) {
        return new InventoryButton()
                .creator(player -> item)
                .consumer(e -> {
                });
    }

}