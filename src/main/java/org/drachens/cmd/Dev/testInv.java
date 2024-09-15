package org.drachens.cmd.Dev;

import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.ItemStackUtil.itemBuilder;
import static org.drachens.util.KyoriUtil.compBuild;

public class testInv extends InventoryGUI {
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, compBuild("test", NamedTextColor.RED));
    }

    public void decorate(@NotNull Player player) {
        this.addButton(0, test(itemBuilder(Material.GOLD_BLOCK, "smth", NamedTextColor.GOLD)));
        this.addButton(0, test(itemBuilder(Material.GOLD_BLOCK, "smth", NamedTextColor.GOLD)));
        this.addButton(3, test(itemBuilder(Material.GOLD_BLOCK, "smth", NamedTextColor.GOLD)));
        super.decorate(player);
    }

    private InventoryButton test(ItemStack material) {
        return new InventoryButton()
                .creator(player -> ItemStack.of(material.material()))
                .consumer(event -> {
                    Player player = event.getPlayer();
                    player.closeInventory();
                });
    }
}
