package org.drachens.generalGame.troops.frontlineinv.frontlinelist;

import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.interfaces.inventories.ExitItem;
import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class FrontLineListInventory implements HotbarInventory {
    private final int start;
    public FrontLineListInventory(int row){
        this.start=row*9;
    }
    @Override
    public void addPlayer(CPlayer player) {

    }

    @Override
    public List<HotbarItemButton> getItems(CPlayer player) {
        List<HotbarItemButton> hotbarItemButtons = new ArrayList<>();
        TroopCountry troopCountry = (TroopCountry) player.getCountry();
        if (!troopCountry.getFrontlines().isEmpty()){
            for (int i = start; i < Math.min(start+8,troopCountry.getFrontlines().size()); i++){
                hotbarItemButtons.add(new FrontLineSelect(troopCountry.getFrontLine(i).getName()));
            }
        }
        hotbarItemButtons.add(new FrontLineCreate());
        hotbarItemButtons.add(new ExitItem());
        return hotbarItemButtons;
    }
}
