package org.drachens.dataClasses;

import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.Manager.per_instance.vote.VotingManager;
import org.drachens.dataClasses.other.ClientEntsToLoad;

public record WorldClasses(CountryDataManager countryDataManager, ClientEntsToLoad clientEntsToLoad, VotingManager votingManager, ProvinceManager provinceManager) { }
