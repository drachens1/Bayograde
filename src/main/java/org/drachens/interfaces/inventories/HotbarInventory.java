package org.drachens.interfaces.inventories;

import org.drachens.player_types.CPlayer;

import java.util.List;

public interface HotbarInventory {
    void addPlayer(CPlayer player);

    List<HotbarItemButton> getItems(CPlayer player);
}
