package org.drachens.cmd;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Research.ResearchGUI;

public class TechCMD extends Command {
    public TechCMD() {
        super("tech");

        setDefaultExecutor((sender,context)->{
            ContinentalManagers.guiManager.openGUI(new ResearchGUI(0,0),(CPlayer) sender);
        });
    }
}
