package org.drachens.events;

import net.minestom.server.entity.Player;
import org.drachens.interfaces.Event;
import org.drachens.interfaces.VotingOption;

public class VoteEvent extends Event {
    private final Player p;
    private final VotingOption voted;

    public VoteEvent(Player p, VotingOption votingOption) {
        this.p = p;
        this.voted = votingOption;
    }

    public Player getPlayer() {
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