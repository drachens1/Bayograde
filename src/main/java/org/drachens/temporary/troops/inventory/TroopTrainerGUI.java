package org.drachens.temporary.troops.inventory;

import dev.ng5m.CPlayer;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import org.drachens.InventorySystem.InventoryGUI;
import org.jetbrains.annotations.NotNull;

public class TroopTrainerGUI extends InventoryGUI{
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW,"Trainer");
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        super.decorate(player);
    }
}
