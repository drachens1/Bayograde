package org.drachens.store.gui;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.store.CosmeticsManager;
import org.drachens.store.StoreItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static org.drachens.util.InventoryUtil.*;
import static org.drachens.util.ItemStackUtil.itemBuilder;

public class CosmeticInventory extends InventoryGUI {
    CosmeticsManager cosmeticsManager = ContinentalManagers.cosmeticsManager;

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, "Cosmetic inventory");
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        outlineInventory(this, outline(itemBuilder(Material.BLUE_STAINED_GLASS_PANE, Component.text("", NamedTextColor.AQUA))));
        addExitButton(this);
        addPlayerHeadAtSlot(this, 4);
        this.addButton(3, addGoldView());
        List<String> cosmetics = player.getOwnedCosmetics();
        for (int i = 0; i < cosmetics.size(); i++) {
            StoreItem storeItem = cosmeticsManager.getStoreItem(cosmetics.get(i));
            if (storeItem == null) {
                i -= 1;
                continue;
            }
            this.addButton(noneEdge[i], cosmetic(storeItem));
        }
        super.decorate(player);
    }

    private InventoryButton addGoldView() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.GOLD_INGOT)
                        .customName(Component.text(player.getGold() + "", NamedTextColor.GOLD, TextDecoration.BOLD))
                        .build())
                .consumer(e -> {
                });
    }

    private InventoryButton outline(ItemStack item) {
        return new InventoryButton()
                .creator(player -> item)
                .consumer(e -> {
                });
    }

    private InventoryButton cosmetic(StoreItem storeItem) {
        return new InventoryButton()
                .creator(e->storeItem.getBoughtItem())
                .consumer(e -> storeItem.clickAfterBought((CPlayer) e.getPlayer()));
    }
}
