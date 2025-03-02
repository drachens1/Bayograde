package org.drachens.generalGame.troops.frontlineinv.frontlinelist;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;
import org.drachens.Manager.decorational.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.dataClasses.Armys.Frontline;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class FrontLineCreate extends HotbarItemButton {
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;

    public FrontLineCreate() {
        super(itemBuilder(Material.EMERALD, Component.text("Create")));
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
        TroopCountry troopCountry = (TroopCountry) p.getCountry();
        String name = troopCountry.getFrontlineHashMap().size()+"";
        Frontline f = new Frontline(name);
        troopCountry.addFrontLine(name,f);
        troopCountry.addActiveFrontLine(p,name);
        inventoryManager.assignInventory(p, InventoryEnum.front_line_create);
        f.addViewer(p);
    }
}
