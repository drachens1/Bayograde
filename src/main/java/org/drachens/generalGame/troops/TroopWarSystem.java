package org.drachens.generalGame.troops;

import net.minestom.server.event.player.PlayerBlockInteractEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.generalGame.troops.buildings.Barracks;
import org.drachens.interfaces.War;
import org.drachens.player_types.CPlayer;

public class TroopWarSystem implements War {

    @Override
    public void onClick(PlayerBlockInteractEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        if (!p.isSneaking()) return;
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getBlockPosition());
        if (province == null || province.getBuilding() == null) return;
        Country country = p.getCountry();
        if (country == null || country != p.getCountry()) return;
        if (province.getBuilding().getBuildTypes() != BuildingEnum.barracks) return;
        Barracks barracks = (Barracks) BuildingEnum.barracks.getBuildTypes();
        barracks.openGui(p, province.getBuilding());
    }

    @Override
    public void onClick(PlayerUseItemEvent e) {

    }

    @Override
    public void onClick(PlayerStartDiggingEvent e) {

    }
}
