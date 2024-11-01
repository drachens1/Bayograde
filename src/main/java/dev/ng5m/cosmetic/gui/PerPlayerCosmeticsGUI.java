package dev.ng5m.cosmetic.gui;

import dev.ng5m.gui.GUIUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.util.ItemStackUtil;
import org.jetbrains.annotations.NotNull;

public final class PerPlayerCosmeticsGUI extends InventoryGUI {
    private final GUIUtils guiUtils = new GUIUtils(this);

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, Component.text("Cosmetics"));
    }

    @Override
    public void decorate(@NotNull Player player) {
        guiUtils.makeBorder((x, y) -> ItemStackUtil.itemBuilder(Material.ORANGE_STAINED_GLASS_PANE));
        super.decorate(player);
    }
}
