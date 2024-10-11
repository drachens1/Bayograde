package org.drachens.cmd.vote;

import net.minestom.server.command.builder.Command;

import java.util.List;

public class VoteCMD extends Command {
    public VoteCMD(List<VotingOptionCMD> votingOptionsCMD){
        super("vote","v");
        setDefaultExecutor((sender,context)->sender.sendMessage("Proper usage: /vote "));
        votingOptionsCMD.forEach(this::addSubcommand);
    }
}
