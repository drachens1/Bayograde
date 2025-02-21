package org.drachens.cmd.gamecreate.creation;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.customgame.CustomGameWorld;
import org.drachens.player_types.CPlayer;

public class CancelCreationCMD extends Command {
    public CancelCreationCMD() {
        super("cancel");

        setCondition((sender, s)->{
            CPlayer p = (CPlayer) sender;
            return p.isLeaderOfOwnGame();
        });

        setDefaultExecutor((sender, context)->{
            CPlayer p = (CPlayer) sender;
            if (!p.isLeaderOfOwnGame()) return;
            CustomGameWorld customGameWorld = (CustomGameWorld) ContinentalManagers.worldManager.getWorld(p.getInstance());
            customGameWorld.delete();
        });
    }
}
