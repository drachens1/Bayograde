package org.drachens.generalGame.factory;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.ClientSideExtras;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.TextDisplay;
import org.drachens.interfaces.inventories.BuildItem;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class FactoryButton extends BuildItem {
    public FactoryButton() {
        super(itemBuilder(Material.CYAN_DYE, 1), BuildingEnum.factory);
    }

    @Override
    public void additionalOnSwapFrom(PlayerChangeHeldSlotEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Country country = p.getCountry();
        if (null == country) return;
        List<Clientside> clientsides = new ArrayList<>();
        p.getClientSideExtras(ClientSideExtras.factory_built).forEach(Clientside::dispose);
        p.removeClientSides(ClientSideExtras.factory_built,clientsides);
    }

    @Override
    public void additionalOnSwapTo(PlayerChangeHeldSlotEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Country country = p.getCountry();
        if (null == country) return;
        List<Clientside> newClientSides = new ArrayList<>();
        Factory factory = (Factory) BuildingEnum.factory.getBuildTypes();
        List<Building> facs = country.getEconomy().getBuildingType(BuildingEnum.factory);
        if (null == facs) return;
        facs.forEach(factor-> newClientSides.add(
                new TextDisplay.create(factor.getProvince().getPos().add(0.5,2,0.5), factor.getCountry().getInstance(),
                        Component.text()
                                .append(Component.text(factor.getCurrentLvl()))
                                .append(Component.text("/"))
                                .append(Component.text(factory.getMaxLvl(factor)))
                                .build())
                .setFollowPlayer(true).build()));

        newClientSides.forEach(newClientSide-> newClientSide.addViewer(p));
        p.addClientSides(ClientSideExtras.factory_built,newClientSides);
    }
}
