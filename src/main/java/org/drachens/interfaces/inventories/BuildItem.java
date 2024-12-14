package org.drachens.interfaces.inventories;

import dev.ng5m.CPlayer;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.territories.Province;
import org.drachens.interfaces.items.HotbarItemButton;

public class BuildItem extends HotbarItemButton {
    private final BuildTypes buildTypes;

    public BuildItem(int modelData, ItemStack item, BuildingEnum buildingEnum) {
        super(modelData, item);
        buildTypes= buildingEnum.getBuildTypes();
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {


    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        Country country = ((CPlayer) e.getPlayer()).getCountry();
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getPosition());
        if (country == null || province == null) return;
        if (province.getBuilding() != null && province.getBuilding().getBuildTypes()==buildTypes.getIdentifier()) {
            province.getBuilding().upgrade(1, country, e.getPlayer());
        } else {
            buildTypes.build(country, province, e.getPlayer());
        }

    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {

    }
}
