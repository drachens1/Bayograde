package org.drachens.cmd.gamecreate.creation;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class CreationCMD extends Command {
    public CreationCMD() {
        super("creation");

        setCondition((sender,s)->{
            CPlayer p = (CPlayer) sender;
            return p.isLeaderOfOwnGame();
        });

        addSubcommand(new CancelCreationCMD());
        addSubcommand(new CompleteCreationCMD());
    }
}
