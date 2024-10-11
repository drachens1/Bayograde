package org.drachens.interfaces.items;

import net.minestom.server.item.ItemStack;

public abstract class HotbarItemButton implements HotbarItem {
    int modelData;
    ItemStack item;
    public HotbarItemButton(int modelData, ItemStack item){
        this.modelData = modelData;
        this.item = item;
    }
    public ItemStack getItem(){
        return item;
    }
    public int getModelData(){
        return modelData;
    }
}
