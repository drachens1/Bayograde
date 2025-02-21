package org.drachens.InventorySystem;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.Inventory;
import org.drachens.player_types.CPlayer;

import java.util.HashMap;
import java.util.Map;

public class GUIManager {
    private final Map<AbstractInventory, InventoryHandler> activeInventories = new HashMap<>();

    public void openGUI(InventoryGUI gui, CPlayer player) {
        this.registerHandledInventory(gui.getInventory(), gui);
        player.openInventory(gui.getInventory());
    }

    public void registerHandledInventory(Inventory inventory, InventoryHandler handler) {
        this.activeInventories.put(inventory, handler);
    }

    public void unregisterInventory(Inventory inventory) {
        this.activeInventories.remove(inventory);
    }

    public void handleClick(InventoryPreClickEvent e) {
        InventoryHandler handler = this.activeInventories.get(e.getInventory());
        if (null != handler) handler.onClick(e);

    }

    public void handleOpen(InventoryOpenEvent e) {
        InventoryHandler handler = this.activeInventories.get(e.getInventory());
        if (null != handler) handler.onOpen(e);
    }

    public void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getNewInventory();
        InventoryHandler handler = this.activeInventories.get(inventory);
        if (null != handler) {
            handler.onClose(event);
            this.unregisterInventory(inventory);
        }
    }
}
