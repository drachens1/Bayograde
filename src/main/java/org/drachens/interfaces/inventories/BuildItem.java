package org.drachens.interfaces.inventories;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.event.player.PlayerUseItemOnBlockEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.ClientSideExtras;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.player_types.CPlayer;

import java.util.List;

public class BuildItem extends HotbarItemButton {
    private final BuildingEnum buildingEnum;

    public BuildItem(ItemStack item, BuildingEnum buildingEnum) {
        super(item);
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

    @Override
    public void onSwapTo(PlayerChangeHeldSlotEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Point point = e.getPlayer().getTargetBlockPosition(5);
        Pos pos;
        if (point == null){
            pos = new Pos(0,0,0);
        }else {
            pos = new Pos(point);
        }
        ItemDisplay itemDisplay = new ItemDisplay(buildingEnum.getBuildTypes().getCan(),pos , ItemDisplay.DisplayType.NONE, p.getInstance(),true);
        itemDisplay.addViewer(p);
        p.addClientSide(ClientSideExtras.can_build,itemDisplay);
    }

    @Override
    public void onSwapFrom(PlayerChangeHeldSlotEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        p.removeClientSideExtras(ClientSideExtras.can_build);
    }

    @Override
    public void onUse(PlayerStartDiggingEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        List<Clientside> clientsides = p.getClientSideExtras(ClientSideExtras.can_build);
        if (clientsides.isEmpty())return;
        ItemDisplay i = (ItemDisplay) clientsides.getFirst();
        i.addYaw(90f);
    }

    @Override
    public void onMove(PlayerMoveEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        Country country = p.getCountry();
        Point point = e.getPlayer().getTargetBlockPosition(10);
        Pos pos;
        if (point == null)return;
        pos = new Pos(point);
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(pos);
        List<Clientside> clientsides = p.getClientSideExtras(ClientSideExtras.can_build);
        if (clientsides.isEmpty())return;
        ItemDisplay i = (ItemDisplay) clientsides.getFirst();
        if (pos == i.getPos())return;
        if (country == null || province == null) {
            return;
        }
        i.setPos(pos.add(0.5,1.5,0.5));
        if (buildingEnum.getBuildTypes().canBuild(country,province,null)){
            i.setItem(buildingEnum.getBuildTypes().getCan());
        }else {
            i.setItem(buildingEnum.getBuildTypes().getCant());
        }
    }
}
