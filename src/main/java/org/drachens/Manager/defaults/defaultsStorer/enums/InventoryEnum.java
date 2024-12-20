package org.drachens.Manager.defaults.defaultsStorer.enums;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.NoneCustomisableInventory;
import org.drachens.interfaces.inventories.BuildItem;
import org.drachens.interfaces.inventories.ChangeInventoryButton;
import org.drachens.interfaces.items.HotbarInventory;
import org.drachens.interfaces.items.HotbarItemButton;
import org.drachens.temporary.demand.DemandInventory;
import org.drachens.temporary.inventories.ExitItem;
import org.drachens.temporary.scoreboards.items.ShowDiplomacy;
import org.drachens.temporary.scoreboards.items.ShowEconomy;
import org.drachens.temporary.scoreboards.items.ShowGeneralInfo;
import org.drachens.temporary.scoreboards.items.ShowIdeology;
import org.drachens.temporary.troops.inventory.TroopDeployer;
import org.drachens.temporary.troops.inventory.TroopMover;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public enum InventoryEnum {
    scoreboardInv(new NoneCustomisableInventory(new HotbarItemButton[]{new ShowDiplomacy(), new ShowEconomy(), new ShowIdeology(), new ShowGeneralInfo(), new ExitItem()})),

    demand(new DemandInventory()),

    research(new NoneCustomisableInventory(new HotbarItemButton[]{new BuildItem(1, itemBuilder(Material.BROWN_DYE, 1), BuildingEnum.library),
            new BuildItem(2, itemBuilder(Material.BROWN_DYE, 2), BuildingEnum.university), new BuildItem(3, itemBuilder(Material.BROWN_DYE, 3), BuildingEnum.researchCenter),
            new BuildItem(4, itemBuilder(Material.BROWN_DYE, 4), BuildingEnum.researchLab), new ExitItem()})),

    defaultInv(new NoneCustomisableInventory(new HotbarItemButton[]{new BuildItem(10, itemBuilder(Material.CYAN_DYE, 10), BuildingEnum.factory),
            new ChangeInventoryButton(0, itemBuilder(Material.BOOK),
                    InventoryEnum.scoreboardInv), new ChangeInventoryButton(1, itemBuilder(Material.BROWN_DYE), InventoryEnum.research)})),

    troops_default(new NoneCustomisableInventory(new HotbarItemButton[]{new BuildItem(10, itemBuilder(Material.CYAN_DYE, 10), BuildingEnum.factory),
            new TroopMover(), new TroopDeployer(), new ChangeInventoryButton(0, itemBuilder(Material.BOOK), InventoryEnum.scoreboardInv),
            new ChangeInventoryButton(1, itemBuilder(Material.BROWN_DYE), InventoryEnum.research), new BuildItem(11,itemBuilder(Material.ACACIA_BOAT),BuildingEnum.barracks)}));

    private final HotbarInventory hotbarInventory;

    InventoryEnum(HotbarInventory hotbarInventory) {
        this.hotbarInventory = hotbarInventory;
    }

    public HotbarInventory getHotbarInventory() {
        return hotbarInventory;
    }
}
