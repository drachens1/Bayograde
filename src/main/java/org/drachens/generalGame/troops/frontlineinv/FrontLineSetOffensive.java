package org.drachens.generalGame.troops.frontlineinv;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.Frontline;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class FrontLineSetOffensive extends HotbarItemButton {
    protected FrontLineSetOffensive() {
        super(itemBuilder(Material.EMERALD, Component.text("Set Attack Point")));
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        CPlayer p = onUse.player();
        TroopCountry country = (TroopCountry) p.getCountry();
        Frontline frontline = country.getActiveFrontLine(p);
        frontline.setAtk(ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(onUse.pos()));
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        CPlayer p = onUse.player();
        TroopCountry country = (TroopCountry) p.getCountry();
        Frontline frontline = country.getActiveFrontLine(p);
        frontline.removeAtk(ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(onUse.pos()));
    }
}
