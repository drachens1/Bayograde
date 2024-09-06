package org.drachens.InventorySystem;

import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;

public interface InventoryHandler {

    void onOpen(InventoryOpenEvent event);

    void onClose(InventoryCloseEvent event);

    void onClick(InventoryClickEvent event);
}
