package org.drachens.cmd;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import org.drachens.InventorySystem.GUIManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.store.gui.CosmeticInventory;

public class CosmeticsCMD extends Command {
    private final GUIManager guiManager = ContinentalManagers.guiManager;

    public CosmeticsCMD() {
        super("cosmetics");
        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            guiManager.openGUI(new CosmeticInventory(), p);
        });
    }
}
