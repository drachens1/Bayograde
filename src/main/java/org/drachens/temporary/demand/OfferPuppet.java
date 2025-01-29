package org.drachens.temporary.demand;

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
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class OfferPuppet extends HotbarItemButton {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public OfferPuppet() {
        super(itemBuilder(Material.IRON_SWORD, Component.text("Offer Puppet", NamedTextColor.AQUA)));
    }

    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(e.getPosition());
        if (province == null) return;
        Demand demand = demandManager.getDemand(p.getCountry());
        Country from = p.getCountry();
        Country provinceCountry = province.getOccupier();
        if (!(from == provinceCountry || from.getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.removePuppetsOffer(provinceCountry);
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Province province = ContinentalManagers.world(p.getInstance()).provinceManager().getProvince(e.getBlockPosition());
        if (province == null) return;
        Demand demand = demandManager.getDemand(p.getCountry());
        Country from = p.getCountry();
        Country provinceCountry = province.getOccupier();
        if (!(from == provinceCountry || from.getPuppets().contains(provinceCountry))) {
            p.sendMessage("They are not a puppet or the actual country");
            return;
        }
        WW2Demands ww2Demands = (WW2Demands) demand;
        ww2Demands.addPuppetOffer(provinceCountry);
    }
}
