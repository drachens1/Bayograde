package org.drachens.InventorySystem;

import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryClickEvent;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class InventoryGUI implements InventoryHandler {

    private final Inventory inventory;
    private final Map<Integer, InventoryButton> buttonMap = new HashMap<>();

    public InventoryGUI() {
        this.inventory = this.createInventory();
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }

    public void decorate(@NotNull Player player) {
        this.buttonMap.forEach((slot, button) -> {
            ItemStack icon = button.getIconCreator().apply(player);
            this.inventory.setItemStack(slot,icon);
        });
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.getPlayer().openInventory(Objects.requireNonNull(event.getInventory()));
        int slot = event.getSlot();
        InventoryButton button = this.buttonMap.get(slot);
        if (button != null) {
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.decorate(event.getPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    protected abstract Inventory createInventory();
}