package org.drachens.dataClasses.Research;

import dev.ng5m.CPlayer;
import net.minestom.server.entity.Player;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.territories.Province;

import java.util.List;

public abstract class ResearchBuilding extends BuildTypes {
    protected final BuildingEnum researchCenter = BuildingEnum.researchCenter;
    public ResearchBuilding(int[] lvls, Material material, BuildingEnum identifier) {
        super(lvls, material, identifier);
    }

    @Override
    public void onBuild(Country country, Province province, Player p) {
        if (!canBuild(country,province,p))return;
        new Building(this,province);
        province.getBuilding().getItemDisplay().addViewer((CPlayer) p);

    }
    public abstract Payment generate(Building building);
}
