package org.drachens.dataClasses.customgame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.player_types.CPlayer;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.InventoryUtil.addExitButton;
import static org.drachens.util.InventoryUtil.outlineInventory;
import static org.drachens.util.ItemStackUtil.itemBuilder;

public class SettingsGUI extends InventoryGUI {
    private boolean instaBuild = false;
    private boolean AIEnabled = true;
    private boolean factionsEnabled = true;
    private boolean researchEnabled = true;
    private long speed = 1L;
    private long progressionRate = 1L;
    private final String votingOption;

    public SettingsGUI(String votingOption){
        this.votingOption=votingOption;
    }
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, "Cosmetic inventory");
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        outlineInventory(this, outline(itemBuilder(Material.BLUE_STAINED_GLASS_PANE, Component.text("", NamedTextColor.AQUA))));
        addExitButton(this);
        addButton(10,instaBuildOption());
        addButton(12,aiEnabledOption());
        addButton(14,factionsEnabledOption());
        addButton(16,researchEnabledOption());
        super.decorate(player);
    }

    private InventoryButton outline(ItemStack item) {
        return new InventoryButton()
                .creator(player -> item)
                .consumer(e -> {
                });
    }

    private InventoryButton instaBuildOption(){
        return new InventoryButton()
                .creator(player -> {
                    if (instaBuild){
                        return itemBuilder(Material.GREEN_CONCRETE, Component.text("Insta Build"));
                    }else {
                        return itemBuilder(Material.RED_CONCRETE, Component.text("Insta Build"));
                    }
                })
                .consumer(e ->{
                    instaBuild=!instaBuild;
                    getInventory().update();
                });
    }

    private InventoryButton aiEnabledOption() {
        return new InventoryButton()
                .creator(player -> {
                    if (AIEnabled) {
                        return itemBuilder(Material.GREEN_CONCRETE, Component.text("AI Enabled"));
                    } else {
                        return itemBuilder(Material.RED_CONCRETE, Component.text("AI Enabled"));
                    }
                })
                .consumer(e -> {
                    AIEnabled = !AIEnabled;
                    getInventory().update();
                });
    }

    private InventoryButton factionsEnabledOption() {
        return new InventoryButton()
                .creator(player -> {
                    if (factionsEnabled) {
                        return itemBuilder(Material.GREEN_CONCRETE, Component.text("Factions Enabled"));
                    } else {
                        return itemBuilder(Material.RED_CONCRETE, Component.text("Factions Enabled"));
                    }
                })
                .consumer(e -> {
                    factionsEnabled = !factionsEnabled;
                    getInventory().update();
                });
    }

    private InventoryButton researchEnabledOption() {
        return new InventoryButton()
                .creator(player -> {
                    if (researchEnabled) {
                        return itemBuilder(Material.GREEN_CONCRETE, Component.text("Research Enabled"));
                    } else {
                        return itemBuilder(Material.RED_CONCRETE, Component.text("Research Enabled"));
                    }
                })
                .consumer(e -> {
                    researchEnabled = !researchEnabled;
                    getInventory().update();
                });
    }
}
