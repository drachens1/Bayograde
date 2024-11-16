package org.drachens.store.hat;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;
import org.drachens.store.StoreItem;

public class CoolHat extends StoreItem {
    public CoolHat(String identifier, int cost, Material material, Component name) {
        super(identifier, cost, material, name);
    }

    @Override
    public void onPurchase(CPlayer p) {

    }
}
