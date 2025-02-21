package org.drachens.generalGame.demand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.Material;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.inventories.HotbarItemButton;
import org.drachens.interfaces.inventories.OnUse;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class DemandPuppet extends HotbarItemButton {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandPuppet() {
        super(itemBuilder(Material.IRON_SWORD, Component.text("Demand Puppet", NamedTextColor.AQUA)));
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        CPlayer p = onUse.player();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(onUse.pos());
        if (province == null) return;
        Demand demand = demandManager.getDemand(p.getCountry());
        Country to = demand.getToCountry();
        Country provinceCountry = province.getOccupier();
        if (!(to == provinceCountry || to.getDiplomacy().getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.removePuppetsDemand(provinceCountry);
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        CPlayer p = onUse.player();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(onUse.pos());
        if (province == null) return;
        Demand demand = demandManager.getDemand(p.getCountry());
        Country to = demand.getToCountry();
        Country provinceCountry = province.getOccupier();
        if (!(to == provinceCountry || to.getDiplomacy().getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.addPuppetDemand(provinceCountry);
    }
}
