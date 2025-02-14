package org.drachens.cmd.gamecreate;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class SaveCustomGameCMD extends Command {
    public SaveCustomGameCMD() {
        super("save");

        setCondition((sender,s)->{
            CPlayer p = (CPlayer) sender;
            return p.isLeaderOfOwnGame()&&!p.isInInterchange();
        });

        setDefaultExecutor((sender,context)->{

        });
    }
}
