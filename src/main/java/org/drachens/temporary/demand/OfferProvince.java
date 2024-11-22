package org.drachens.temporary.demand;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.items.HotbarItemButton;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.KyoriUtil.compBuild;

public class OfferProvince extends HotbarItemButton {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public OfferProvince() {
        super(1, itemBuilder(Material.IRON_SWORD, compBuild("Offer province", NamedTextColor.AQUA)));
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {

    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(e.getPosition());
        if (province == null) return;
        Demand demand = demandManager.getDemand(p);
        Country from = p.getCountry();
        Country provinceCountry = province.getOccupier();
        if (!(from == provinceCountry || from.getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.removeProvinceOffer(province);
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        p.sendMessage("2");
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(e.getBlockPosition());
        if (province == null) return;
        Demand demand = demandManager.getDemand(p);
        Country from = p.getCountry();
        Country provinceCountry = province.getOccupier();
        if (!(from == provinceCountry || from.getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.addProvinceOffer(province);
    }
}