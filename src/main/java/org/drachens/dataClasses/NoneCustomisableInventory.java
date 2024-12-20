package org.drachens.dataClasses;

import net.minestom.server.entity.Player;
import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;

public class NoneCustomisableInventory extends HotbarInventory {
    public NoneCustomisableInventory(HotbarItemButton[] hotbarItemButtons) {
        super(hotbarItemButtons);
    }

    @Override
    protected void addPlayer(Player player) {
    }
}
