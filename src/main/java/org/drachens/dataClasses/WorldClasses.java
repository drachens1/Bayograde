package org.drachens.dataClasses;

import org.drachens.Manager.CountryDataManager;
import org.drachens.Manager.MapGeneratorManager;
import org.drachens.Manager.YearManager;
import org.drachens.dataClasses.other.ClientEntsToLoad;

public record WorldClasses(YearManager yearManager, CountryDataManager countryDataManager,
                           MapGeneratorManager mapGeneratorManager, ClientEntsToLoad clientEntsToLoad) {
}
