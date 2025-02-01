package org.drachens.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import org.drachens.dataClasses.VotingOption;

public record VoteEvent(Player p, VotingOption voted) implements Event { }