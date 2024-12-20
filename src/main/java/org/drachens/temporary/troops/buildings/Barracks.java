package org.drachens.temporary.troops.buildings;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.animation.Animation;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.other.ItemDisplay;
import org.drachens.dataClasses.territories.Province;
import org.drachens.temporary.troops.inventory.TroopTrainerGUI;

public class Barracks extends BuildTypes {
    private final Animation trainingAnimation = new Animation(500, Material.ORANGE_DYE,new int[]{23,24,25,26});
    private final Payment payment = new Payment(CurrencyEnum.production, 10f);
    private final Component cantAffordMsg = Component.text()
            .append(Component.text("You cannot afford the barracks : 5 Production", NamedTextColor.RED))
            .build();
    public Barracks() {
        super(new int[]{22}, Material.ORANGE_DYE, BuildingEnum.barracks);
    }

    @Override
    public void onBuild(Country country, Province province, Player p) {
        Building building = new Building(this,province);
        ItemDisplay itemDisplay = building.getItemDisplay();
        trainingAnimation.start(itemDisplay,true);
    }

    @Override
    public boolean canBuild(Country country, Province province, Player p) {
        if (province.getOccupier() != country) return false;
        if (province.getBuilding() != null) return false;
        if (!province.isCity()) return false;
        if (!country.canMinusCost(payment)) {
            p.sendMessage(cantAffordMsg);
            return false;
        }
        return true;
    }

    public void openGui(CPlayer p, Building building){
        ContinentalManagers.guiManager.openGUI(new TroopTrainerGUI(),p);
    }
}
