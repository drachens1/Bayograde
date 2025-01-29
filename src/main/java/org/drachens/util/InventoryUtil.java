package org.drachens.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class InventoryUtil {
    public static final int[] noneEdge = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
    private static final InventoryButton exit = new InventoryButton()
            .creator(player -> itemBuilder(Material.BARRIER, Component.text("Exit", NamedTextColor.RED, TextDecoration.BOLD)))
            .consumer(e -> e.getPlayer().closeInventory());
    private static final InventoryButton head = new InventoryButton()
            .creator(CPlayer::getPlayerHead)
            .consumer(e -> e.setCancelled(true));

    public static void addExitButton(InventoryGUI inventoryGUI) {
        inventoryGUI.addButton(inventoryGUI.getInventory().getSize() - 1, exit);
    }

    public static void outlineInventory(InventoryGUI inventory, InventoryButton inventoryButton) {
        int rows = 6;
        int columns = 9;
        for (int i = 0; i < rows * columns; i++) {
            int row = i / columns;
            int col = i % columns;

            if (row == 0 || row == rows - 1 || col == 0 || col == columns - 1) {
                inventory.addButton(i, inventoryButton);
            }
        }
    }

    public static void addPlayerHeadAtSlot(InventoryGUI inventory, int slot) {
        inventory.addButton(slot, head);
    }
}
