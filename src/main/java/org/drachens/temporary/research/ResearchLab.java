package org.drachens.temporary.research;

import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.Manager.defaults.defaultsStorer.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Research.ResearchBuilding;
import org.drachens.dataClasses.territories.Province;

public class ResearchLab extends ResearchBuilding {
    private final Payment cost = new Payment(CurrencyEnum.production,2f);
    public ResearchLab() {
        super(new int[]{4}, Material.BROWN_DYE, BuildingEnum.researchLab);
    }

    @Override
    public Payment generate(Building building) {
        return new Payment(CurrencyEnum.research,5f);
    }

    @Override
    public boolean canBuild(Country country, Province province, Player p) {
        if (province.getOccupier()!=country||province.getBuilding()!=null)return false;
        if (!country.canMinusCost(cost))return false;
        for (Province neighbour : province.getNeighbours()){
            if (neighbour.getBuilding()==null || neighbour.getOccupier()!=country)continue;
            if (neighbour.getBuilding().getBuildTypes()==researchCenter){
                return true;
            }
        }
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
}
