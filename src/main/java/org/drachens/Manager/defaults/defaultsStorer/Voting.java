package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.Manager.defaults.defaultsStorer.enums.VotingWinner;
import org.drachens.interfaces.VotingOption;

import java.util.HashMap;

public class Voting {
    HashMap<VotingWinner, VotingOption> votingOptionHashMap = new HashMap<>();

    public void register(VotingOption votingOption, VotingWinner name) {
        this.votingOptionHashMap.put(name, votingOption);
    }

    public void unregister(VotingWinner name) {
        votingOptionHashMap.remove(name);
    }

    public VotingOption getVotingOption(VotingWinner name) {
        return votingOptionHashMap.get(name);
    }

    public HashMap<VotingWinner, VotingOption> getVotingOptionHashMap() {
        return votingOptionHashMap;
    }
}
