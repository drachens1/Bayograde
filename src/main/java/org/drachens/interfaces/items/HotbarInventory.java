package org.drachens.interfaces.items;

import net.minestom.server.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class HotbarInventory {
    private final List<HotbarItemButton> hotbarItemButtons = new ArrayList<>();

    public HotbarInventory(HotbarItemButton[] hotbarItemButtons) {
        this.hotbarItemButtons.addAll(List.of(hotbarItemButtons));
    }

    protected abstract void addPlayer(Player player);

    public List<HotbarItemButton> getItems() {
        return hotbarItemButtons;
    }
}
