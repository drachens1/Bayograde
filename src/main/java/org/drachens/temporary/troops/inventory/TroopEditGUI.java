package org.drachens.temporary.troops.inventory;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Armys.DivisionType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static org.drachens.util.InventoryUtil.addExitButton;
import static org.drachens.util.InventoryUtil.outlineInventory;
import static org.drachens.util.ItemStackUtil.itemBuilder;

public class TroopEditGUI extends InventoryGUI {
    private final DivisionDesign design;
    private final HashMap<Integer, DivisionType> divTypeHash;
    private final int[] coords = new int[]{10,19,28,37,12,13,14,15,16,21,22,23,24,25,30,31,32,33,34,39,40,41,42,43};

    public TroopEditGUI(HashMap<Integer, DivisionType> dHashMap, DivisionDesign design) {
        this.divTypeHash=dHashMap;
        this.design=design;
    }

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, Component.text("Edit"));
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        outlineInventory(this,sideButtons());
        for (int i : coords){
            if (divTypeHash.containsKey(i)){
                addButton(i, selectDivisionType(divTypeHash.get(i)));
            }else {
                addButton(i, selectDivisionTypeEmpty());

            }
        }
        for (int i = 11; i < 39; i += 9){
            addButton(i, sideButtons());
        }
        addButton(44, saveChanges());
        addExitButton(this);
        super.decorate(player);
    }

    protected InventoryButton sideButtons() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.BLUE_STAINED_GLASS_PANE)
                        .customName(Component.text(" "))
                        .build());
    }

    protected InventoryButton selectDivisionTypeEmpty(){
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.ORANGE_STAINED_GLASS_PANE)
                        .customName(Component.text("Something"))
                        .build())
                .consumer(e -> {divTypeClick(e);});
    }
    protected InventoryButton selectDivisionType(DivisionType divisionTypeEnum){
        return new InventoryButton()
                .creator(player -> divisionTypeEnum.getIcon())
                .consumer(e -> {divTypeClick(e);});
    }

    protected InventoryButton saveChanges(){
        return new InventoryButton()
                .creator(player -> itemBuilder(Material.GREEN_CONCRETE,Component.text("Save Changes")))
                .consumer(e -> {
                    design.setDesign(divTypeHash);
                });
    }

    private void divTypeClick(InventoryPreClickEvent e){
        ContinentalManagers.guiManager.openGUI(new TroopTypeSelectionGUI(divTypeHash,design,e.getSlot()), (CPlayer) e.getPlayer());
    }
}
