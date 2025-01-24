package org.drachens.Manager.defaults;

import org.drachens.dataClasses.VotingOption;

public enum VotingWinner {
    none,
    ww2_clicks,
    ww2_troops;

    private VotingOption votingOption;

    public VotingOption getVotingOption() {
        return votingOption;
    }

    public void setVotingOption(VotingOption votingOption) {
        this.votingOption = votingOption;
    }
}
