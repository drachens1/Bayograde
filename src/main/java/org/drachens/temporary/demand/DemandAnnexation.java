package org.drachens.temporary.demand;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.inventories.HotbarItemButton;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class DemandAnnexation extends HotbarItemButton {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandAnnexation() {
        super(3, itemBuilder(Material.IRON_SWORD, Component.text("Demand Annexation", NamedTextColor.AQUA)));
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(e.getPosition());
        if (province == null) return;
        Demand demand = demandManager.getDemand(p.getCountry());
        Country to = demand.getToCountry();
        Country provinceCountry = province.getOccupier();
        if (!(to == provinceCountry || to.getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.removeAnnexationDemand(provinceCountry);
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(e.getBlockPosition());
        if (province == null) return;
        Demand demand = demandManager.getDemand(p.getCountry());
        Country to = demand.getToCountry();
        Country provinceCountry = province.getOccupier();
        if (!(to == provinceCountry || to.getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.addAnnexationDemand(provinceCountry);
    }
}
