package org.drachens.generalGame.troops.frontlineinv;

import net.minestom.server.item.Material;
import org.drachens.Manager.decorational.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class FrontLineExit extends HotbarItemButton {
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;

    public FrontLineExit() {
        super(itemBuilder(Material.BARRIER, 10));
    }

    @Override
    public void onLeftClick(OnUse onUse) {
        used(onUse);
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        used(onUse);
    }

    @Override
    public void onRightClick(OnUse onUse) {
        used(onUse);
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        used(onUse);
    }

    public void used(OnUse onUse){
        CPlayer p = onUse.player();
        TroopCountry country = (TroopCountry) p.getCountry();
        country.getActiveFrontLine(p).removeViewer(p);
        inventoryManager.assignInventory(onUse.player(), ContinentalManagers.world(onUse.instance()).dataStorer().votingOption.getDefaultInventory());
    }
}
