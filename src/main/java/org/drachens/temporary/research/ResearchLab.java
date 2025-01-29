package org.drachens.temporary.research;

import org.drachens.player_types.CPlayer;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.Research.ResearchBuilding;

public class ResearchLab extends ResearchBuilding {
    private final Payment cost = new Payment(CurrencyEnum.production, 2f);

    public ResearchLab() {
        super(new int[]{4}, Material.BROWN_DYE, BuildingEnum.researchLab);
    }

    @Override
    public Payment generate(Building building) {
        return new Payment(CurrencyEnum.research, 5f);
    }

    @Override
    public boolean canBuild(Country country, Province province, CPlayer p) {
        if (province.getOccupier() != country || province.getBuilding() != null) return false;
        if (!country.canMinusCost(cost)) return false;
        for (Province neighbour : province.getNeighbours()) {
            if (neighbour.getBuilding() == null || neighbour.getOccupier() != country) continue;
            if (neighbour.getBuilding().getBuildTypes() == researchCenter) {
                return true;
            }
        }
        return false;
    }
}
