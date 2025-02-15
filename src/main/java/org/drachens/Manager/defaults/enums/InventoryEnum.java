package org.drachens.Manager.defaults.enums;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.NoneCustomisableInventory;
import org.drachens.generalGame.clicks.ClicksDefaultInventory;
import org.drachens.generalGame.demand.*;
import org.drachens.generalGame.factory.FactoryButton;
import org.drachens.generalGame.scoreboards.items.ShowDiplomacy;
import org.drachens.generalGame.scoreboards.items.ShowEconomy;
import org.drachens.generalGame.scoreboards.items.ShowGeneralInfo;
import org.drachens.generalGame.scoreboards.items.ShowIdeology;
import org.drachens.generalGame.troops.TroopsDefaultInventory;
import org.drachens.interfaces.inventories.BuildItem;
import org.drachens.interfaces.inventories.ExitItem;
import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public enum InventoryEnum {
    scoreboardInv(new NoneCustomisableInventory(new HotbarItemButton[]{new ShowDiplomacy(), new ShowEconomy(), new ShowIdeology(), new ShowGeneralInfo(), new ExitItem()})),

    demand(new NoneCustomisableInventory(new HotbarItemButton[]{new DemandProvince(), new DemandPuppet(), new DemandAnnexation(), new OfferProvince(), new OfferPuppet(), new OfferAnnexation()})),

    research(new NoneCustomisableInventory(new HotbarItemButton[]{new BuildItem(itemBuilder(Material.BROWN_DYE, 1), BuildingEnum.library),
            new BuildItem(itemBuilder(Material.BROWN_DYE, 2), BuildingEnum.university), new BuildItem(itemBuilder(Material.BROWN_DYE, 3), BuildingEnum.researchCenter),
            new BuildItem(itemBuilder(Material.BROWN_DYE, 4), BuildingEnum.researchLab), new ExitItem()})),

    clicks_build_menu(new NoneCustomisableInventory(new HotbarItemButton[]{new FactoryButton(),new ExitItem()})),

    defaultInv(new ClicksDefaultInventory()),

    troops_build_menu(new NoneCustomisableInventory(new HotbarItemButton[]{new FactoryButton(),new BuildItem(itemBuilder(Material.GOLD_INGOT), BuildingEnum.barracks),new ExitItem()})),

    troops_default(new TroopsDefaultInventory());


    private final HotbarInventory hotbarInventory;

    InventoryEnum(HotbarInventory hotbarInventory) {
        this.hotbarInventory = hotbarInventory;
    }

    public HotbarInventory getHotbarInventory() {
        return hotbarInventory;
    }
}
