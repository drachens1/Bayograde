package org.drachens.Manager;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;

public class GeneralManager {
    public boolean factionsEnabled(Instance instance){
        return null == ContinentalManagers.world(instance) || null == ContinentalManagers.world(instance).dataStorer().votingOption || ContinentalManagers.world(instance).dataStorer().votingOption.isFactionsEnabled();
    }
    public boolean aiEnabled(Instance instance){
        return null == ContinentalManagers.world(instance) || null == ContinentalManagers.world(instance).dataStorer().votingOption || ContinentalManagers.world(instance).dataStorer().votingOption.isAIEnabled();
    }
    public boolean researchEnabled(Instance instance){
        return null == ContinentalManagers.world(instance) || null == ContinentalManagers.world(instance).dataStorer().votingOption || ContinentalManagers.world(instance).dataStorer().votingOption.isResearchEnabled();
    }
    public boolean votingEnabled(Instance instance){
        return null == ContinentalManagers.world(instance) || ContinentalManagers.world(instance).isGlobalGameWorldClass();
    }
}
