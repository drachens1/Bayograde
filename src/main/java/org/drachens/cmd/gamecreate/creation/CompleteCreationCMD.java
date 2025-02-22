package org.drachens.cmd.gamecreate.creation;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.customgame.CustomGameWorld;
import org.drachens.player_types.CPlayer;

public class CompleteCreationCMD extends Command {
    public CompleteCreationCMD() {
        super("complete");

        setDefaultExecutor((sender, context)->{
            CPlayer p = (CPlayer) sender;
            CustomGameWorld customGameWorld = (CustomGameWorld) ContinentalManagers.worldManager.getWorld(p.getInstance());
            customGameWorld.complete();
        });
    }
}
