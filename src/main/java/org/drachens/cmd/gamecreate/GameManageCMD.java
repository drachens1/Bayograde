package org.drachens.cmd.gamecreate;

import net.minestom.server.command.builder.Command;
import org.drachens.cmd.gamecreate.creation.CreationCMD;
import org.drachens.player_types.CPlayer;

public class GameManageCMD extends Command {
    public GameManageCMD() {
        super("manage");

        setCondition((sender,s)->{
            CPlayer p = (CPlayer) sender;
            return p.isLeaderOfOwnGame();
        });

        addSubcommand(new AddPlayerCustomGameCMD());
        addSubcommand(new CreationCMD());
        addSubcommand(new KickPlayerCustomGameCMD());
        addSubcommand(new GameOptionsManageOptionsCMD());
        addSubcommand(new SaveCustomGameCMD());
    }
}
