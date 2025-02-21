package org.drachens.InventorySystem;

import lombok.Getter;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.player_types.CPlayer;

import java.util.function.Consumer;
import java.util.function.Function;

@Getter
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

}