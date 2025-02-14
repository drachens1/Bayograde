package org.drachens.cmd.gamecreate.creation;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.customgame.InterchangeWorld;
import org.drachens.player_types.CPlayer;

public class CancelCreationCMD extends Command {
    public CancelCreationCMD() {
        super("cancel");

        setCondition((sender,s)->{
            CPlayer p = (CPlayer) sender;
            return p.isLeaderOfOwnGame();
        });

        setDefaultExecutor((sender,context)->{
            CPlayer p = (CPlayer) sender;
            if (!p.isLeaderOfOwnGame())return;
            InterchangeWorld interchangeWorld = (InterchangeWorld) ContinentalManagers.worldManager.getWorld(p.getInstance());
            interchangeWorld.delete();
        });
    }
}
