package org.drachens.events.System;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.instance.Instance;
import org.drachens.interfaces.Voting.VotingOption;

public class StartGameEvent implements Event, CancellableEvent {
    private final Instance instance;
    private final VotingOption votingOption;
    public StartGameEvent(Instance instance, VotingOption votingOption) {
        this.instance = instance;
        this.votingOption = votingOption;
    }
    public Instance getInstance(){
        return instance;
    }
    public VotingOption getVotingOption(){
        return votingOption;
    }
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}