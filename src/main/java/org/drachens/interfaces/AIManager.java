package org.drachens.interfaces;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.VotingWinner;
import org.drachens.dataClasses.Countries.Country;

public interface AIManager {
    VotingWinner getIdentifier();

    AI createAIForCountry(Country country);
    
    void tick(Instance instance);
} 
