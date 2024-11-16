package org.drachens.InventorySystem;

import dev.ng5m.CPlayer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class InventoryButton {
    private Function<CPlayer, ItemStack> iconCreator;
    private Consumer<InventoryPreClickEvent> eventConsumer;

    public InventoryButton creator(Function<CPlayer, ItemStack> iconCreator) {
        this.iconCreator = iconCreator;
        return this;
    }

    public InventoryButton consumer(Consumer<InventoryPreClickEvent> eventConsumer) {
        this.eventConsumer = eventConsumer;
        return this;
    }

    public Consumer<InventoryPreClickEvent> getEventConsumer() {
        return this.eventConsumer;
    }

    public Function<CPlayer, ItemStack> getIconCreator() {
        return this.iconCreator;
    }
}