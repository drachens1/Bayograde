package org.drachens.temporary;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class test extends InventoryGUI {
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW,"smth");
    }

    @Override
    public void decorate(@NotNull Player player) {
        this.addButton(0,button(itemBuilder(Material.CYAN_DYE)));
        super.decorate(player);
    }
    private InventoryButton button(ItemStack material) {
        return new InventoryButton()
                .creator(player -> material)
                .consumer(e -> {
                    Player player = e.getPlayer();
                    player.closeInventory();
                });
    }
}
