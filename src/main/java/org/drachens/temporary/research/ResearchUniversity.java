package org.drachens.temporary.research;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.Manager.defaults.defaultsStorer.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.Research.ResearchBuilding;

public class ResearchUniversity extends ResearchBuilding {
    private final Payment cost = new Payment(CurrencyEnum.production,5f);

    public ResearchUniversity() {
        super(new int[]{2}, Material.BROWN_DYE, BuildingEnum.university);
    }

    @Override
    public boolean canBuild(Country country, Province province, Player p) {

        if (province.getOccupier()!=country)return false;
        if (!country.canMinusCost(cost))return false;
        for (Province neighbour : province.getNeighbours()){
            if (neighbour.getBuilding()==null || neighbour.getOccupier()!=country)continue;
            if (neighbour.getBuilding().getBuildTypes()==researchCenter){
                return true;
            }
        }
        System.out.println(4);
        return false;
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
    protected void onCaptured(Country capturer, Building bRuilding) {

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

    @Override
    public Payment generate(Building building) {
        if (building.getBuildTypes()!=getIdentifier())return new Payment(CurrencyEnum.research,0f);
        return new Payment(CurrencyEnum.research,5f);
    }
}
