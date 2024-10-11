package org.drachens.dataClasses;

import org.drachens.interfaces.items.HotbarItemButton;

import java.util.ArrayList;
import java.util.List;

public class HotbarInventory {
    private final List<HotbarItemButton> hotbarItemButtons = new ArrayList<>();
    public HotbarInventory(HotbarItemButton[] hotbarItemButtons){
        this.hotbarItemButtons.addAll(List.of(hotbarItemButtons));
    }
    public List<HotbarItemButton> getItems(){
        return hotbarItemButtons;
    }
}
