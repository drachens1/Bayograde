package org.drachens.generalGame.troops.frontlineinv;

import net.minestom.server.item.Material;
import org.drachens.Manager.decorational.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.generalGame.troops.frontlineinv.frontlinelist.FrontLineListInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class FrontLineOpener extends HotbarItemButton {
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;
    public FrontLineOpener() {
        super(itemBuilder(Material.EMERALD));
    }

    @Override
    public void onRightClick(OnUse onUse) {
        used(onUse);
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        used(onUse);
    }

    @Override
    public void onLeftClick(OnUse onUse) {
        used(onUse);
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        used(onUse);
    }

    private void used(OnUse onUse){
        CPlayer p = onUse.player();
        Country country = p.getCountry();
        if (country==null)return;
        inventoryManager.changeInventory(p,new FrontLineListInventory(0));
    }
}
