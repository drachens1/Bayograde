package org.drachens.interfaces;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.dataClasses.Countries.countryClass.Country;

public interface AIManager {
    VotingWinner getIdentifier();

    AI createAIForCountry(Country country);

    void tick(Instance instance);
} 
