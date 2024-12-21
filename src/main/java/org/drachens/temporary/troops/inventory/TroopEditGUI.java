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

import static org.drachens.util.InventoryUtil.addExitButton;
import static org.drachens.util.InventoryUtil.outlineInventory;
import static org.drachens.util.ItemStackUtil.itemBuilder;

import java.util.HashMap;
import java.util.Map;

public class TroopEditGUI extends InventoryGUI {
    private final DivisionDesign design;
    private HashMap<Integer, DivisionType> divTypeHash = new HashMap<>();

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
        for (Map.Entry<Integer, DivisionType> e : divTypeHash.entrySet()){
            if (e.getValue()==null){
                addButton(e.getKey(), selectDivisionTypeEmpty());
            }else{
                addButton(e.getKey(), selectDivisionType(e.getValue()));
            }
        }
        for (int i = 11; i < 38; i += 9){
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
