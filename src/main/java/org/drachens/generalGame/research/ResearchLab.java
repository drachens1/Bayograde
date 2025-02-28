package org.drachens.generalGame.research;

import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.Research.ResearchBuilding;
import org.drachens.player_types.CPlayer;

public class ResearchLab extends ResearchBuilding {
    private final Payment cost = new Payment(CurrencyEnum.production, 2.0f);

    public ResearchLab() {
        super(new int[]{4}, Material.BROWN_DYE, BuildingEnum.researchLab);
    }

    @Override
    public Payment getCost() {
        return cost;
    }

    @Override
    public Payment generate(Building building) {
        return new Payment(CurrencyEnum.research, 5.0f);
    }

    @Override
    public boolean canBuild(Country country, Province province, CPlayer p) {
        if (province.getOccupier() != country || null != province.getBuilding()) return false;
        if (!country.canMinusCost(cost)) return false;
        for (Province neighbour : province.getNeighbours()) {
            if (null == neighbour.getBuilding() || neighbour.getOccupier() != country) continue;
            if (this.researchCenter == neighbour.getBuilding().getBuildTypes()) {
                return true;
            }
        }
        return false;
    }
}
