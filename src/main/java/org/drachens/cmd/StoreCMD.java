package org.drachens.cmd;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.store.gui.Store;

public class StoreCMD extends Command {
    private final GUIManager guiManager = ContinentalManagers.guiManager;

    public StoreCMD() {
        super("store");

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            guiManager.openGUI(new Store(), p);
        });
    }
}
