package org.drachens.cmd.Dev;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.defaults.VotingWinner;

public class RegenCMD extends Command {
    public RegenCMD() {
        super("regen");

        var baseScale = ArgumentType.Double("Scale");

        addSyntax((sender,context)->{
            CPlayer player = (CPlayer) sender;
            VotingWinner.ww2_clicks.getVotingOption().getMapGenerator().generate(player.getInstance(),VotingWinner.ww2_clicks.getVotingOption());
        },baseScale);
    }
}
