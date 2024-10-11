package org.drachens.interfaces;

import net.minestom.server.instance.Instance;
import org.drachens.interfaces.Voting.VotingOption;

public interface MapGenerator {
    void generate(Instance instance, VotingOption votingOption);
}
