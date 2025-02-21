package org.drachens.store;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;

import java.util.Arrays;
import java.util.List;

@Getter
public class StoreCategory {
    private final List<StoreItem> storeItems;
    private final String identifier;
    private final Component name;
    private final ItemStack item;

    public StoreCategory(String identifier, Component name, ItemStack item, StoreItem... storeItems) {
        this.identifier = identifier;
        this.name = name;
        this.item = item;
        this.storeItems = Arrays.stream(storeItems).toList();
    }

}
