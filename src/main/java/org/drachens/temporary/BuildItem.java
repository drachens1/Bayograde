package org.drachens.temporary;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerUseItemEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.PlaceableBuilds;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.factory.PlaceableFactory;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.interfaces.items.HotbarItemButton;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class BuildItem extends HotbarItemButton {
    private final Payment cost = new Payment(ContinentalManagers.defaultsStorer.currencies.getCurrencyType("production"),5f);
    private final Component cantAffordMsg = Component.text()
            .append(Component.text("You cannot afford the factory : 5 Production", NamedTextColor.RED))
            .build();
    public BuildItem() {
        super(10, itemBuilder(Material.CYAN_DYE, 10));
    }

    @Override
    public void onUse(PlayerUseItemEvent e) {
        e.getPlayer().sendMessage("hi");

    }
    @Override
    public void onUse(PlayerUseItemOnBlockEvent e) {
        e.getPlayer().sendMessage("bye");
        Country country = getCountryFromPlayer(e.getPlayer());
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(e.getPosition());
        if (!(province.getOccupier() == country && province.isCity())) return;
        if (!country.canMinusCost(cost)){
            e.getPlayer().sendMessage(cantAffordMsg);
            return;
        }
        if (province.getBuildType() == null) {
            new PlaceableFactory(ContinentalManagers.defaultsStorer.placeables.getFactoryType("civilian"), province);
        }else {
            province.getBuildType().onUpgrade(1);
        }
    }
}