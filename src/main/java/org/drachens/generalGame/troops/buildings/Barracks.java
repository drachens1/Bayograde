package org.drachens.generalGame.troops.buildings;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.generalGame.troops.inventory.TroopTrainerGUI;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.Messages.sendMessage;

public class Barracks extends BuildTypes {
    private final Payment payment = new Payment(CurrencyEnum.production, 10f);
    private final Component cantAffordMsg = Component.text()
            .append(Component.text("You cannot afford the barracks : 5 Production", NamedTextColor.RED))
            .build();

    public Barracks() {
        super(new int[]{19}, Material.ORANGE_DYE, BuildingEnum.barracks,
                province -> itemBuilder(Material.ORANGE_DYE,24),
                province -> itemBuilder(Material.ORANGE_DYE,25));
    }

    @Override
    public void onBuild(Country country, Province province, CPlayer p) {
        new Building(this, province);
    }

    @Override
    public void onBuild(Country country, Province province, CPlayer p, float yaw) {
        new Building(this, province).getItemDisplay().addYaw(yaw);
    }

    @Override
    public boolean canBuild(Country country, Province province, CPlayer p) {
        if (province.getOccupier() != country) return false;
        if (!country.canMinusCost(payment)) {
            if (p!=null) p.sendMessage(cantAffordMsg);
            return false;
        }
        return true;
    }

    public void startTraining(Building building, DivisionDesign divisionDesign, CPlayer p) {
        if (!building.getProvince().getOccupier().canMinusCosts(divisionDesign.getCost())) {
            sendMessage(p, Component.text("You cannot afford this"));
            return;
        }
        TroopCountry troopCountry = (TroopCountry) building.getCountry();
        troopCountry.getBuildingsTraining(building).addToQueue(divisionDesign);
    }

    public void openGui(CPlayer p, Building building) {
        ContinentalManagers.guiManager.openGUI(new TroopTrainerGUI(building), p);
    }
}
