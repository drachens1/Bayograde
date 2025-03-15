package org.drachens.generalGame.troops.frontlineinv;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.Armys.Frontline;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class FrontLineSetAtk extends HotbarItemButton {
    public FrontLineSetAtk() {
        super(itemBuilder(Material.EMERALD, Component.text("Set Attacking")));
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        used(onUse);
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        used(onUse);
    }

    private void used(OnUse onUse){
        CPlayer p = onUse.player();
        TroopCountry country = (TroopCountry) p.getCountry();
        Frontline frontline = country.getActiveFrontLine(p);
        frontline.setAtking(!frontline.isAtking());
        p.sendMessage("Attacking: "+frontline.isAtking());
    }
}
