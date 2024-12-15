package org.drachens.temporary.research;

import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.Manager.defaults.defaultsStorer.enums.CurrencyEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.Vault;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Research.ResearchCenter;

import java.util.List;
import java.util.Map;

public abstract class ResearchVault extends Vault {
    private final Currencies research = new Currencies(CurrencyEnum.research, 0f);
    private final ResearchCenter researchCenter = (ResearchCenter) BuildingEnum.researchCenter.getBuildTypes();
    private ResearchCountry country;

    public ResearchVault(Map<CurrencyTypes, Currencies> startingCurrencies) {
        super(startingCurrencies);
    }

    @Override
    public void onCountrySet(Country country) {
        this.country = (ResearchCountry) country;
    }

    @Override
    public void extraCalcIncrease() {
        Payment central = new Payment(CurrencyEnum.research, 0f);
        for (Building building : country.getResearchCentersBuildings()) {
            central.add(researchCenter.generate(building));
        }
        research.set(central.getAmount());
    }

    public Payment getResearch() {
        return new Payment(research);
    }

    @Override
    protected List<Currencies> getCustomCurrencies() {
        return List.of(research);
    }
}
