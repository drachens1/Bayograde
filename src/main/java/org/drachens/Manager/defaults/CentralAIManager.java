package org.drachens.Manager.defaults;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.events.CustomTick;
import org.drachens.events.NewDay;
import org.drachens.interfaces.ai.AIManager;

import java.util.HashMap;

import static org.drachens.util.OtherUtil.runThread;

public class CentralAIManager {
    private final HashMap<VotingWinner, AIManager> aiManagers = new HashMap<>();

    public CentralAIManager(){
        MinecraftServer.getGlobalEventHandler().addListener(NewDay.class, e ->
                runThread(()->aiManagers.get(ContinentalManagers.world(e.world()).dataStorer().votingWinner).tick(e.world())));
        MinecraftServer.getGlobalEventHandler().addListener(CustomTick.class, e -> runThread(()->
                runThread(()->aiManagers.get(ContinentalManagers.world(e.instance()).dataStorer().votingWinner).fasterTick(e.instance()))));
    }

    public void registerEventManager(AIManager aiManager) {
        aiManagers.put(aiManager.getIdentifier(), aiManager);
    }

    public AIManager getAIManagerFor(Instance instance) {
        return aiManagers.get(ContinentalManagers.world(instance).dataStorer().votingWinner);
    }
}
