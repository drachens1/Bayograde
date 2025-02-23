package org.drachens.events;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.VotingOption;
import org.drachens.player_types.CPlayer;

public record VoteEvent(CPlayer p, VotingOption voted) implements Event { }