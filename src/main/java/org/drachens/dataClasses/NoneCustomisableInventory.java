package org.drachens.dataClasses;

import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.player_types.CPlayer;

import java.util.List;

public class NoneCustomisableInventory implements HotbarInventory {
    private final List<HotbarItemButton> hotbarItemButtons;
    public NoneCustomisableInventory(HotbarItemButton[] hotbarItemButtons) {
        this.hotbarItemButtons=List.of(hotbarItemButtons);
    }

    @Override
    public void addPlayer(CPlayer player) {
    }

    @Override
    public List<HotbarItemButton> getItems(CPlayer player) {
        return hotbarItemButtons;
    }
}
