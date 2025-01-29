package org.drachens.cmd.example;

import org.drachens.player_types.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class ExampleGUI extends InventoryGUI {
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, "Example");
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        for (int i = 0; i < getInventory().getSize(); i++) {
            addButton(i, button(itemBuilder(Material.GREEN_CONCRETE, Component.text(i + " Num", NamedTextColor.BLUE))));
        }
        super.decorate(player);
    }

    private InventoryButton button(ItemStack i) {
        return new InventoryButton()
                .creator(player -> i)
                .consumer(e -> {
                });
    }
}
