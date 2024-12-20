package org.drachens.temporary.demand;

import net.minestom.server.entity.Player;
import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;

public class DemandInventory extends HotbarInventory {

    public DemandInventory() {
        super(new HotbarItemButton[]{new DemandProvince(), new DemandPuppet(), new DemandAnnexation(), new OfferProvince(), new OfferPuppet(), new OfferAnnexation()});
    }

    @Override
    protected void addPlayer(Player player) {

    }
}
