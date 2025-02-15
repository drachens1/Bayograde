package org.drachens.generalGame.clicks;

import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.generalGame.invasions.NavalInvasionClicksItem;
import org.drachens.interfaces.inventories.ChangeInventoryButton;
import org.drachens.interfaces.inventories.HotbarInventory;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.player_types.CPlayer;

import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.OtherUtil.addToList;

public class ClicksDefaultInventory implements HotbarInventory {
    private final List<HotbarItemButton> defaultButtons = List.of(new ChangeInventoryButton(itemBuilder(Material.BROWN_DYE), InventoryEnum.clicks_build_menu),
            new ChangeInventoryButton(itemBuilder(Material.BOOK),
                    InventoryEnum.scoreboardInv),
            new NavalInvasionClicksItem());
    public ClicksDefaultInventory() {

    }

    @Override
    public void addPlayer(CPlayer player) {

    }

    @Override
    public List<HotbarItemButton> getItems(CPlayer player) {
        Instance instance = player.getInstance();
        if (ContinentalManagers.generalManager.researchEnabled(instance)){
            return addToList(defaultButtons,new ChangeInventoryButton(itemBuilder(Material.BROWN_DYE), InventoryEnum.research));
        }
        return defaultButtons;
    }
}
