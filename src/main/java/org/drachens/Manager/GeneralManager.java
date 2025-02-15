package org.drachens.Manager;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;

public class GeneralManager {
    public boolean factionsEnabled(Instance instance){
        return ContinentalManagers.world(instance)==null || ContinentalManagers.world(instance).dataStorer().votingOption==null || ContinentalManagers.world(instance).dataStorer().votingOption.isFactionsEnabled();
    }
    public boolean aiEnabled(Instance instance){
        return ContinentalManagers.world(instance)==null || ContinentalManagers.world(instance).dataStorer().votingOption==null || ContinentalManagers.world(instance).dataStorer().votingOption.isAIEnabled();
    }
    public boolean researchEnabled(Instance instance){
        return ContinentalManagers.world(instance)==null || ContinentalManagers.world(instance).dataStorer().votingOption==null || ContinentalManagers.world(instance).dataStorer().votingOption.isResearchEnabled();
    }
    public boolean votingEnabled(Instance instance){
        return ContinentalManagers.world(instance)==null || ContinentalManagers.world(instance).isGlobalGameWorldClass();
    }
}
