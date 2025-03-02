package org.drachens.generalGame.troops.frontlineinv;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.Frontline;
import org.drachens.dataClasses.Province;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class FrontLineProvinces extends HotbarItemButton {
    protected FrontLineProvinces() {
        super(itemBuilder(Material.EMERALD, Component.text("Province Manager")));
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        Province province = ContinentalManagers.world(onUse.instance()).provinceManager().getProvince(onUse.pos());
        CPlayer p = onUse.player();
        if (province==null||p==null)return;
        if (province.getOccupier()==null||p.getCountry()!=province.getOccupier())return;
        TroopCountry troopCountry = (TroopCountry) p.getCountry();
        if (!troopCountry.activeFrontLineContainsPlayer(p))return;
        Frontline frontline = troopCountry.getActiveFrontLine(p);
        frontline.addProvince(province);
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        Province province = ContinentalManagers.world(onUse.instance()).provinceManager().getProvince(onUse.pos());
        CPlayer p = onUse.player();
        if (province==null||p==null)return;
        if (province.getOccupier()==null||p.getCountry()!=province.getOccupier())return;
        TroopCountry troopCountry = (TroopCountry) p.getCountry();
        if (!troopCountry.activeFrontLineContainsPlayer(p))return;
        Frontline frontline = troopCountry.getActiveFrontLine(p);
        frontline.removeProvince(province);
    }
}
