package org.drachens.interfaces;

import org.drachens.dataClasses.Countries.Country;

import net.minestom.server.instance.Instance;

public interface AIManager {
    AI createAIForCountry(Country country);
    
    void tick(Instance instance);
} 
