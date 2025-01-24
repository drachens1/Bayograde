package org.drachens.interfaces.inventories;

import dev.ng5m.CPlayer;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;

public class BuildItem extends HotbarItemButton {
    private final BuildingEnum buildingEnum;

    public BuildItem(int modelData, ItemStack item, BuildingEnum buildingEnum) {
        super(modelData, item);
        this.buildingEnum = buildingEnum;
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        Country country = ((CPlayer) e.getPlayer()).getCountry();
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getPosition());
        if (country == null || province == null) return;
        if (province.getBuilding() != null && province.getBuilding().getBuildTypes() == buildingEnum.getBuildTypes().getIdentifier()) {
            province.getBuilding().upgrade(1, country, (CPlayer) e.getPlayer());
        } else {
            buildingEnum.getBuildTypes().build(country, province, (CPlayer) e.getPlayer());
        }
    }
}
