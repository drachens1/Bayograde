package org.drachens.store;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;

import java.util.Arrays;
import java.util.List;

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

    public String getIdentifier() {
        return identifier;
    }

    public Component getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public List<StoreItem> getStoreItems() {
        return storeItems;
    }
}
