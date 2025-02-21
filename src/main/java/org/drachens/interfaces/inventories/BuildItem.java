package org.drachens.interfaces.inventories;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerChangeHeldSlotEvent;
import net.minestom.server.event.player.PlayerMoveEvent;
import net.minestom.server.item.ItemStack;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.ClientSideExtras;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.player_types.CPlayer;

import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class BuildItem extends HotbarItemButton {
    private final BuildingEnum buildingEnum;

    public BuildItem(ItemStack item, BuildingEnum buildingEnum) {
        super(item);
        this.buildingEnum = buildingEnum;
    }

    @Override
    public void onRightClickOnBlock(OnUse onUse) {
        Country country = onUse.player().getCountry();
        Province province = ContinentalManagers.world(onUse.instance()).provinceManager().getProvince(onUse.pos());
        if (country == null || province == null) return;
        if (province.getBuilding() != null && province.getBuilding().getBuildTypes() == buildingEnum.getBuildTypes().getIdentifier()) {
            province.getBuilding().upgrade(1, country, onUse.player());
        } else {
            List<Clientside> clientsides = onUse.player().getClientSideExtras(ClientSideExtras.can_build);
            if (clientsides.isEmpty()){
                buildingEnum.getBuildTypes().build(country, province, onUse.player());
                return;
            }
            ItemDisplay i = (ItemDisplay) clientsides.getFirst();
            buildingEnum.getBuildTypes().build(country, province, onUse.player(), i.getYaw());
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
        ItemDisplay itemDisplay = new ItemDisplay(itemBuilder(buildingEnum.getBuildTypes().getMaterial()),pos , ItemDisplay.DisplayType.NONE, p.getInstance());
        itemDisplay.addViewer(p);
        p.addClientSide(ClientSideExtras.can_build,itemDisplay);
        additionalOnSwapTo(e);
    }

    public void additionalOnSwapTo(PlayerChangeHeldSlotEvent e){}

    @Override
    public void onSwapFrom(PlayerChangeHeldSlotEvent e) {
        CPlayer p = (CPlayer) e.getPlayer();
        p.removeClientSideExtras(ClientSideExtras.can_build);
        additionalOnSwapFrom(e);
    }

    public void additionalOnSwapFrom(PlayerChangeHeldSlotEvent e){}

    @Override
    public void onLeftClickOnBlock(OnUse onUse) {
        CPlayer p = onUse.player();
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
        if (point == null)return;
        Pos pos = new Pos(point);
        Province province = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(pos);
        List<Clientside> clientsides = p.getClientSideExtras(ClientSideExtras.can_build);
        if (clientsides.isEmpty())return;
        ItemDisplay i = (ItemDisplay) clientsides.getFirst();
        if (pos == i.getPos())return;
        if (country == null || province == null) return;
        i.setPos(pos.add(0.5,1.5,0.5));
        if (province.getBuilding()!=null){
            if (buildingEnum.getBuildTypes().requirementsToUpgrade(province.getBuilding(),country, 1,null)){
                i.setItem(buildingEnum.getBuildTypes().getCanItem(province));
                return;
            }
            i.setItem(buildingEnum.getBuildTypes().getCantItem(province));
            return;
        }
        if (buildingEnum.getBuildTypes().canBuild(country,province,null)){
            i.setItem(buildingEnum.getBuildTypes().getCanItem(province));
            return;
        }
        i.setItem(buildingEnum.getBuildTypes().getCantItem(province));
    }
}
