package org.drachens.events;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import org.drachens.interfaces.Voting.VotingOption;

public class VoteEvent implements Event, CancellableEvent {
    private final Player p;
    private final VotingOption voted;

    public VoteEvent(Player p, VotingOption votingOption) {
        this.p = p;
        this.voted = votingOption;
    }

    public Player getPlayer(){
        return p;
    }

    public VotingOption getVoted() {
        return voted;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}