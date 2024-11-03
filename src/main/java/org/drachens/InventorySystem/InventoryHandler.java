package org.drachens.InventorySystem;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;

public interface InventoryHandler {

    void onOpen(InventoryOpenEvent event);

    void onClose(InventoryCloseEvent event);

    void onClick(InventoryPreClickEvent event);
}
