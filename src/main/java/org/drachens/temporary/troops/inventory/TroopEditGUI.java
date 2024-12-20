package org.drachens.temporary.troops.inventory;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.InventoryUtil.outlineInventory;

public class TroopEditGUI extends InventoryGUI {
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, Component.text("Edit"));
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        outlineInventory(this,sideButtons());
        addButton(11,sideButtons());
        addButton(20,sideButtons());
        addButton(29,sideButtons());
        addButton(38,sideButtons());
        super.decorate(player);
    }

    protected InventoryButton sideButtons() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.BLUE_STAINED_GLASS_PANE)
                        .customName(Component.text(" "))
                        .build());
    }
}
