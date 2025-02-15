package org.drachens.dataClasses.additional;

import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.Manager.per_instance.ProvinceManager;
import org.drachens.Manager.per_instance.vote.VotingManager;
import org.drachens.dataClasses.datastorage.DataStorer;
import org.drachens.dataClasses.datastorage.WorldClasses;
import org.drachens.dataClasses.other.ClientEntsToLoad;

public class GlobalGameWorldClass extends WorldClasses {
    private final VotingManager votingManager;
    public GlobalGameWorldClass(CountryDataManager countryDataManager, ClientEntsToLoad clientEntsToLoad, VotingManager votingManager, ProvinceManager provinceManager, DataStorer dataStorer) {
        super(countryDataManager, clientEntsToLoad,provinceManager, dataStorer);
        this.votingManager=votingManager;
    }
    public VotingManager votingManager(){
        return votingManager;
    }
}
