package org.drachens.dataClasses.Research;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.Manager.defaults.defaultsStorer.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.Province;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.Manager.defaults.ContinentalManagers.defaultsStorer;

public class ResearchCenter extends BuildTypes {
    private final ResearchCenter researchBuilding = (ResearchCenter) defaultsStorer.buildingTypes.getBuildType(BuildingEnum.researchCenter);
    private final List<ResearchBuilding> buildingList = new ArrayList<>();
    private final Component cantAffordMsg = Component.text()
            .append(Component.text("You cannot afford the research center : 5 Production", NamedTextColor.RED))
            .build();

    private final Payments payments = new Payments(new Payment(defaultsStorer.currencies.getCurrencyType("production"), 5f));

    public ResearchCenter() {
        super(new int[]{3}, Material.BROWN_DYE, BuildingEnum.researchCenter);
    }

    @Override
    public void onBuild(Country country, Province province, Player p) {
        country.removePayments(payments);
        new Building(this, province);
    }

    @Override
    public boolean canBuild(Country country, Province province, Player p) {
        if (province.getOccupier() != country) return false;
        if (province.getBuilding() != null) return false;
        if (!country.canMinusCosts(payments)) {
            p.sendMessage(cantAffordMsg);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirementsToUpgrade(Building building, Country country, int add, Player p) {
        return false;
    }

    @Override
    public boolean requirementsToDestroy(Country country) {
        return false;
    }

    @Override
    protected void onCaptured(Country capturer, Building building) {

    }

    @Override
    protected void bombed(float dmg) {

    }

    @Override
    protected void onDestroyed(Building building) {

    }

    @Override
    protected void onUpgrade(int amount, Building building) {

    }

    public void addResearchBuilding(ResearchBuilding researchBuilding){
        buildingList.add(researchBuilding);
    }

    public void removeResearchBuilding(ResearchBuilding researchBuilding){
        buildingList.remove(researchBuilding);
    }

    public Payment generate(Building building){
        Payment central = new Payment(CurrencyEnum.research,0f);
        building.getProvince().getNeighbours().forEach(province -> {
            if (province.getBuilding().getBuildTypes().equals(researchBuilding)){
                central.add(researchBuilding.generate(province.getBuilding()));
            }
        });
        return null;
    }
}
