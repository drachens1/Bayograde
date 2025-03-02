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

public class FrontLineTroops extends HotbarItemButton {
    protected FrontLineTroops() {
        super(itemBuilder(Material.EMERALD, Component.text("Troop Manager")));
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        Province province = ContinentalManagers.world(onUse.instance()).provinceManager().getProvince(onUse.pos());
        CPlayer p = onUse.player();
        if (province==null||p==null||province.getTroops().isEmpty())return;
        if (province.getOccupier()==null||p.getCountry()!=province.getOccupier())return;
        TroopCountry troopCountry = (TroopCountry) p.getCountry();
        if (!troopCountry.activeFrontLineContainsPlayer(p))return;
        Frontline frontline = troopCountry.getActiveFrontLine(p);
        province.getTroops().forEach(frontline::addTroop);
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        Province province = ContinentalManagers.world(onUse.instance()).provinceManager().getProvince(onUse.pos());
        CPlayer p = onUse.player();
        if (province==null||p==null||province.getTroops().isEmpty())return;
        if (province.getOccupier()==null||p.getCountry()!=province.getOccupier())return;
        TroopCountry troopCountry = (TroopCountry) p.getCountry();
        if (!troopCountry.activeFrontLineContainsPlayer(p))return;
        Frontline frontline = troopCountry.getActiveFrontLine(p);
        province.getTroops().forEach(frontline::removeTroop);
    }
}
