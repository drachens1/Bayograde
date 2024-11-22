package org.drachens.events.System;

import net.minestom.server.instance.Instance;
import org.drachens.interfaces.Event;
import org.drachens.interfaces.VotingOption;

public class StartGameEvent extends Event {
    private final Instance instance;
    private final VotingOption votingOption;

    public StartGameEvent(Instance instance, VotingOption votingOption) {
        super(instance);
        this.instance = instance;
        this.votingOption = votingOption;
    }

    public Instance getInstance() {
        return instance;
    }

    public VotingOption getVotingOption() {
        return votingOption;
    }
}