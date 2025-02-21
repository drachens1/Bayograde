package org.drachens.dataClasses.Research;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.dataClasses.Province;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.Messages.sendMessage;

public class ResearchCenter extends BuildTypes {
    private final Component cantAffordMsg = Component.text()
            .append(Component.text("You cannot afford the research center : 5 Production", NamedTextColor.RED))
            .build();

    private final Payments payments = new Payments(new Payment(CurrencyEnum.production, 5f));

    public ResearchCenter() {
        super(new int[]{3}, Material.BROWN_DYE, BuildingEnum.researchCenter);
    }

    @Override
    public void onBuild(Country country, Province province, CPlayer p) {
        country.removePayments(payments);
        new Building(this, province);
        ResearchCountry c = country.getResearch().researchCountry();
        c.addResearchCenter(province.getBuilding());
    }

    @Override
    public boolean canBuild(Country country, Province province, CPlayer p) {
        if (province.getOccupier() != country) return false;
        if (province.getBuilding() != null) return false;
        if (!country.canMinusCosts(payments)) {
            sendMessage(p, cantAffordMsg);
            return false;
        }
        return true;
    }

    @Override
    protected void onCaptured(Country capturer, Building building) {
        ResearchCountry c = building.getCountry().getResearch().researchCountry();
        ResearchCountry s = capturer.getResearch().researchCountry();
        c.removeResearchCenter(building);
        s.addResearchCenter(building);
        building.setCountry(capturer);
    }

    public Payment generate(Building building) {
        Payment central = new Payment(CurrencyEnum.research, 0f);
        for (Province province : building.getProvince().getNeighbours()) {
            if (province.getBuilding() == null) continue;
            if (province.getBuilding().getSynonyms().contains("research")) {
                Building building1 = province.getBuilding();
                if (!(building1.getBuildTypes().getBuildTypes() instanceof ResearchBuilding researchBuilding)) {
                    continue;
                }
                central.add(researchBuilding.generate(building1));
            }
        }
        return central;
    }
}
