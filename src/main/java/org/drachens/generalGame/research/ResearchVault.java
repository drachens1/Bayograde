package org.drachens.generalGame.research;

import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.Manager.defaults.enums.CurrencyEnum;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.dataClasses.Research.ResearchCountry;

import java.util.List;

public class ResearchVault {
    private final Currencies research = new Currencies(CurrencyEnum.research, 0.0f);
    private final ResearchCenter researchCenter = (ResearchCenter) BuildingEnum.researchCenter.getBuildTypes();
    private final ResearchCountry country;

    public ResearchVault(ResearchCountry country) {
        this.country=country;
    }

    public void extraCalcIncrease() {
        Payment central = new Payment(CurrencyEnum.research, 0.0f);
        for (Building building : country.getResearchCentersBuildings()) {
            central.add(researchCenter.generate(building));
        }
        research.set(central.getAmount());
    }

    public Payment getResearch() {
        return new Payment(research);
    }

    protected List<Currencies> getCustomCurrencies() {
        return List.of(research);
    }
}
