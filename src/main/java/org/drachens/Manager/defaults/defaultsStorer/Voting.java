package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.interfaces.Voting.VotingOption;

import java.util.HashMap;

public class Voting {
    HashMap<String, VotingOption> votingOptionHashMap = new HashMap<>();

    public void register(VotingOption votingOption, String name){
        this.votingOptionHashMap.put(name, votingOption);
    }

    public void unregister(String name){
        votingOptionHashMap.remove(name);
    }

    public VotingOption getVotingOption(String name){
        return votingOptionHashMap.get(name);
    }

    public HashMap<String, VotingOption> getVotingOptionHashMap(){
        return votingOptionHashMap;
    }
}
