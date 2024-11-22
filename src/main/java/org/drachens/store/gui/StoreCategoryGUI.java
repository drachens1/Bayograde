package org.drachens.store.gui;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import static org.drachens.util.KyoriUtil.compBuild;

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
        outlineInventory(this, outline(itemBuilder(Material.BLUE_STAINED_GLASS_PANE, compBuild("", NamedTextColor.AQUA))));
        addExitButton(this);
        addPlayerHeadAtSlot(this, 4);
        this.addButton(3, addGoldView());
        super.decorate(p);
    }

    private InventoryButton addGoldView() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.GOLD_INGOT)
                        .customName(compBuild(player.getGold() + "", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .build())
                .consumer(e -> {

                });
    }

    private InventoryButton storeItemButton(StoreItem storeItem, CPlayer p) {
        return new InventoryButton()
                .creator(player -> storeItem.getItem(p))
                .consumer(e -> {
                    if (!storeItem.canBuy(p)) return;
                    storeItem.purchase(p);
                });
    }

    private InventoryButton outline(ItemStack item) {
        return new InventoryButton()
                .creator(player -> item)
                .consumer(e -> {
                });
    }

}