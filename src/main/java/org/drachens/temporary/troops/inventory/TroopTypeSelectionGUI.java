package org.drachens.temporary.troops.inventory;

import static org.drachens.util.InventoryUtil.addExitButton;
import static org.drachens.util.InventoryUtil.outlineInventory;

import java.util.HashMap;

import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.DivisionTypeEnum;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Armys.DivisionType;
import org.jetbrains.annotations.NotNull;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class TroopTypeSelectionGUI extends InventoryGUI {
    private final DivisionDesign design;
    private HashMap<Integer, DivisionType> divTypeHash = new HashMap<>();
    private int slot;

    public TroopTypeSelectionGUI(HashMap<Integer, DivisionType> dHashMap, DivisionDesign design, int slot) {
        this.divTypeHash=dHashMap;
        this.design=design;
        this.slot=slot;
    }

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, Component.text("Edit"));
    }

    @Override
    public void decorate(@NotNull CPlayer p){
        outlineInventory(this, sideButtons());
        addExitButton(this);
        addButton(10, divisionType(DivisionTypeEnum.ww2_artillery));
        addButton(11, divisionType(DivisionTypeEnum.ww2_cavalry));
        addButton(12, divisionType(DivisionTypeEnum.ww2_infantry));
        addButton(13, divisionType(DivisionTypeEnum.ww2_motorized));
        addButton(14, divisionType(DivisionTypeEnum.ww2_motorized_artillery));
        addButton(15, divisionType(DivisionTypeEnum.ww2_tank));
        super.decorate(p);
    }

    protected InventoryButton sideButtons() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.BLUE_STAINED_GLASS_PANE)
                        .customName(Component.text(" "))
                        .build());
    }

    protected InventoryButton divisionType(DivisionTypeEnum divisionType){
        return new InventoryButton()
                .creator(player -> divisionType.getDivisionType().getIcon())
                .consumer(e -> {
                    divTypeHash.put(slot, divisionType.getDivisionType());
                    ContinentalManagers.guiManager.openGUI(new TroopEditGUI(divTypeHash,design), (CPlayer) e.getPlayer());
                });
    }
}
