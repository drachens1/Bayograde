package org.drachens.Manager.defaults;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.interfaces.AIManager;

import java.util.HashMap;

public class CentralAIManager {
    private final HashMap<VotingWinner, AIManager> aiManagers = new HashMap<>();

    public void registerEventManager(AIManager aiManager) {
        aiManagers.put(aiManager.getIdentifier(), aiManager);
    }

    public AIManager getAIManagerFor(Instance instance) {
        return aiManagers.get(ContinentalManagers.world(instance).dataStorer().votingWinner);
    }
}
