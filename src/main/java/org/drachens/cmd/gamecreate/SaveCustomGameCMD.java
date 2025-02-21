package org.drachens.cmd.gamecreate;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

public class SaveCustomGameCMD extends Command {
    public SaveCustomGameCMD() {
        super("save");

        setDefaultExecutor((sender, context)->{
            CPlayer p = (CPlayer) sender;
            ContinentalManagers.saveManager.save(p.getInstance());
        });
    }
}
