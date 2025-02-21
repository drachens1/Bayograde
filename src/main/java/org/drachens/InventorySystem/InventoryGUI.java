package org.drachens.InventorySystem;

import lombok.Getter;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.ItemStack;
import org.drachens.player_types.CPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class InventoryGUI implements InventoryHandler {

    @Getter
    private final Inventory inventory;
    private final Map<Integer, InventoryButton> buttonMap = new HashMap<>();

    protected InventoryGUI() {
        this.inventory = this.createInventory();
    }

    public void addButton(int slot, InventoryButton button) {
        this.buttonMap.put(slot, button);
    }

    public void decorate(@NotNull CPlayer player) {
        this.buttonMap.forEach((slot, button) -> {
            ItemStack icon = button.getIconCreator().apply(player);
            this.inventory.setItemStack(slot, icon);
        });
    }

    @Override
    public void onClick(InventoryPreClickEvent event) {
        event.setCancelled(true);
        event.getPlayer().openInventory((Inventory) Objects.requireNonNull(event.getInventory()));
        int slot = event.getSlot();
        InventoryButton button = this.buttonMap.get(slot);
        if (null != button && null != button.getEventConsumer()) {
            button.getEventConsumer().accept(event);
        }
    }

    @Override
    public void onOpen(InventoryOpenEvent event) {
        this.decorate((CPlayer) event.getPlayer());
    }

    @Override
    public void onClose(InventoryCloseEvent event) {
    }

    protected abstract Inventory createInventory();
}