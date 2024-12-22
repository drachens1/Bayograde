package org.drachens.interfaces;

import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.Countries.Country;

public interface AIManager {
    AI createAIForCountry(Country country);
    
    void tick(Instance instance);
} 
