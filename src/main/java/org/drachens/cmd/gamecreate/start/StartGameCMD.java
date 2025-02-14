package org.drachens.cmd.gamecreate.start;

import net.minestom.server.command.builder.Command;
import org.drachens.cmd.vote.VotingOptionCMD;

import java.util.List;

public class StartGameCMD extends Command {
    public StartGameCMD(List<VotingOptionCMD> votingOptionsCMD) {
        super("start-game");

        addSubcommand(new NewStartGameCMD(votingOptionsCMD));
        addSubcommand(new LoadStartGameCMD());
    }
}
