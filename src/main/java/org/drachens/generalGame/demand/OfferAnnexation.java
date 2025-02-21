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

public class OfferAnnexation extends HotbarItemButton {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public OfferAnnexation() {
        super(itemBuilder(Material.IRON_SWORD, Component.text("Offer Annexation", NamedTextColor.AQUA)));
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        CPlayer p = onUse.player();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(onUse.pos());
        if (null == province) return;
        Demand demand = demandManager.getDemand(p.getCountry());
        Country from = p.getCountry();
        Country provinceCountry = province.getOccupier();
        if (!(from == provinceCountry || from.getDiplomacy().getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.removeAnnexationOffer(provinceCountry);
    }

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        CPlayer p = onUse.player();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(onUse.pos());
        if (null == province) return;
        Demand demand = demandManager.getDemand(p.getCountry());
        Country from = p.getCountry();
        Country provinceCountry = province.getOccupier();
        if (!(from == provinceCountry || from.getDiplomacy().getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.addAnnexationOffer(provinceCountry);
    }
}
