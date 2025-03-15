package org.drachens.generalGame.troops.frontlineinv;

import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.player_types.CPlayer;

import java.util.List;

public class FrontLineInventory implements HotbarInventory {
    List<HotbarItemButton> hotbarItemButtons;
    public FrontLineInventory(){
        hotbarItemButtons=List.of(new HotbarItemButton[]
                {new FrontLineProvinces(), new FrontLineTroops(), new FrontLineSetAtk(), new FrontLineSetOffensive(), new FrontLineExit()});
    }

    @Override
    public void addPlayer(CPlayer player) {

    }

    @Override
    public List<HotbarItemButton> getItems(CPlayer player) {
        return hotbarItemButtons;
    }
}
