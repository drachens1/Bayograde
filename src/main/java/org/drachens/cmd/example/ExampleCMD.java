package org.drachens.cmd.example;

import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.player_types.CPlayer;

public class ExampleCMD extends Command {
    public ExampleCMD() {
        super("example");
        setDefaultExecutor((sender, context) -> ContinentalManagers.guiManager.openGUI(new ExampleGUI(), (CPlayer) sender));
    }
}
