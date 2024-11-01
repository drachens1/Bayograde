package org.drachens.temporary;

import dev.ng5m.CPlayer;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.BuildTypes;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.interfaces.items.HotbarItemButton;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class BuildItem extends HotbarItemButton {
    private BuildTypes buildTypes = ContinentalManagers.defaultsStorer.buildingTypes.getBuildType("factory");

    public BuildItem() {
        super(10, itemBuilder(Material.CYAN_DYE, 10));
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {
        e.getPlayer().sendMessage("hi");

    }
    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        Country country = ((CPlayer) e.getPlayer()).getCountry();
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getPosition());
        if (country == null || province==null)return;
        if (province.getBuilding()!=null){
            province.getBuilding().upgrade(1,country,e.getPlayer());
        }else {
            buildTypes.build(country,province,e.getPlayer());
        }

    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {

    }
}