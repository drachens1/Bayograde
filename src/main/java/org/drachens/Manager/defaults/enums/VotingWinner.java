package org.drachens.Manager.defaults.enums;

import lombok.Getter;
import lombok.Setter;
import org.drachens.dataClasses.VotingOption;

@Getter
public enum VotingWinner {
    none,
    ww2_clicks,
    ww2_troops;

    @Setter
    private VotingOption votingOption;
}
