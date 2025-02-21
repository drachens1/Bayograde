package org.drachens.cmd.vote;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

import java.util.List;

public class VoteCMD extends Command {
    public VoteCMD(List<VotingOptionCMD> votingOptionsCMD) {
        super("vote", "v");
        setCondition((sender, s)->{
            CPlayer p = (CPlayer) sender;
            return ContinentalManagers.generalManager.votingEnabled(p.getInstance());
        });
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /vote "));
        votingOptionsCMD.forEach(this::addSubcommand);
    }
}
