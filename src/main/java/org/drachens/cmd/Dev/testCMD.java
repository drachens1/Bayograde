package org.drachens.cmd.Dev;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import org.drachens.InventorySystem.GUIManager;

public class testCMD extends Command {
    public testCMD(GUIManager guiManager) {
        super("test");
        setDefaultExecutor((sender,context)->{
            if (!(sender instanceof Player p)){
                return;
            }
            guiManager.openGUI(new testInv(),p);
        });

    }
}
