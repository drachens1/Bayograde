package org.drachens.store.items;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.store.StoreItem;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Hat extends StoreItem {
    private final ItemStack itemStack;

    public Hat(String identifier, int cost, Material material, Component name, int modelData) {
        super(identifier, cost, material, name, modelData);
        itemStack = itemBuilder(material, name, modelData);
    }

    @Override
    public void onPurchase(CPlayer p) {
        p.setHelmet(itemStack);
    }

    @Override
    protected void onClickAfterBought(CPlayer p) {

    }
}
