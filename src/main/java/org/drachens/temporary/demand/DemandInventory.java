package org.drachens.temporary.demand;

import net.minestom.server.entity.Player;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.interfaces.items.HotbarInventory;
import org.drachens.interfaces.items.HotbarItemButton;

public class DemandInventory extends HotbarInventory {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandInventory() {
        super(new HotbarItemButton[]{new DemandProvince(), new DemandPuppet(), new DemandAnnexation(), new OfferProvince(), new OfferPuppet(), new OfferAnnexation()});
    }

    @Override
    protected void addPlayer(Player player) {

    }
}
