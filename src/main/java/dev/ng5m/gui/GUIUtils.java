package dev.ng5m.gui;

import net.minestom.server.item.ItemStack;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;

import java.util.function.BiFunction;

public final class GUIUtils {
    private final InventoryGUI inventoryGUI;

    public GUIUtils(InventoryGUI inventoryGUI) {
        this.inventoryGUI = inventoryGUI;
    }

    public void makeBorder(BiFunction<Integer, Integer, ItemStack> item) {
        final int size = inventoryGUI.getInventory().getSize();
        final int rows = size / 9;

        for (int x = 0; x < 9; x++) {
            final int fx = x;
            inventoryGUI.addButton(x, new InventoryButton().creator(player -> item.apply(fx, 0)));
            inventoryGUI.addButton((rows - 1) * 9 + x, new InventoryButton()
                    .creator(player -> item.apply(fx, rows - 1))
                    .consumer(event -> {
                        System.out.println("Clicked");
                        event.setCancelled(true);
                    }));
        }

        for (int y = 1; y < rows - 1; y++) {
            inventoryGUI.getInventory().setItemStack(y * 9, item.apply(0, y));
            inventoryGUI.getInventory().setItemStack(y * 9 + 8, item.apply(8, y));
        }
    }

}
