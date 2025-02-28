package org.drachens.events.system;

import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;
import org.drachens.dataClasses.VotingOption;

public record StartGameEvent(Instance instance, VotingOption votingOption) implements Event {}