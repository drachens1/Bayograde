package org.drachens.cmd.example;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;

public class ExampleCMD extends Command {
    public ExampleCMD() {
        super("example");
        setDefaultExecutor(((sender, context) -> ContinentalManagers.guiManager.openGUI(new ExampleGUI(), (CPlayer) sender)));
    }
}
