package org.drachens.generalGame.troops.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import org.drachens.dataClasses.ComponentListBuilder;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.player_types.CPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static org.drachens.util.InventoryUtil.addExitButton;
import static org.drachens.util.InventoryUtil.outlineInventory;
import static org.drachens.util.ItemStackUtil.itemBuilder;

public class TroopEditGUI extends InventoryGUI {
    private final DivisionDesign design;
    private final HashMap<Integer, DivisionType> divTypeHash;
    private final int[] coords = {11, 12, 13, 14, 15, 20, 21, 22, 23, 24, 29, 30, 31, 32, 33, 38, 39, 40, 41, 42};

    public TroopEditGUI(HashMap<Integer, DivisionType> dHashMap, DivisionDesign design) {
        this.divTypeHash = dHashMap;
        this.design = design;
    }

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, Component.text("Edit"));
    }

    @Override
    public void decorate(@NotNull CPlayer player) {
        outlineInventory(this, sideButtons());
        for (int i : coords) {
            if (divTypeHash.containsKey(i)) {
                addButton(i, selectDivisionType(divTypeHash.get(i)));
            } else {
                addButton(i, selectDivisionTypeEmpty());
            }
        }
        for (int i = 16; 44 > i; i += 9) {
            addButton(i, sideButtons());
        }
        for (int i = 10; 39 > i; i += 9) {
            addButton(i, sideButtons());
        }
        addButton(4, viewStats());
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

    protected InventoryButton selectDivisionTypeEmpty() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.ORANGE_STAINED_GLASS_PANE)
                        .customName(Component.text("Something"))
                        .build())
                .consumer(this::divTypeClick);
    }

    protected InventoryButton selectDivisionType(DivisionType divisionTypeEnum) {
        return new InventoryButton()
                .creator(player -> divisionTypeEnum.getIcon())
                .consumer(this::divTypeClick);
    }

    protected InventoryButton saveChanges() {
        return new InventoryButton()
                .creator(player -> itemBuilder(Material.GREEN_CONCRETE, Component.text("Save Changes")))
                .consumer(e -> design.setDesign(divTypeHash));
    }

    protected InventoryButton viewStats() {
        return new InventoryButton()
                .creator(player -> {
                    float hp = 0.0f;
                    float atk = 0.0f;
                    float def = 0.0f;
                    float speed = 0.0f;
                    float org = 0.0f;
                    Payments paymentList = new Payments();
                    for (Map.Entry<Integer, DivisionType> e : divTypeHash.entrySet()) {
                        DivisionType d = e.getValue();
                        hp += d.getHp();
                        atk += d.getAtk();
                        def += d.getDef();
                        speed += d.getSpeed();
                        org += d.getOrg();
                        paymentList.addPayment(d.getCost());
                    }
                    paymentList.compress();
                    return itemBuilder(Material.BOOK, Component.text("View Stats", NamedTextColor.GOLD, TextDecoration.BOLD, TextDecoration.UNDERLINED), new ComponentListBuilder()
                            .addComponent(Component.text()
                                    .append(Component.text("Atk: "))
                                    .append(Component.text(atk))
                                    .build())
                            .addComponent(Component.text()
                                    .append(Component.text("HP: "))
                                    .append(Component.text(hp))
                                    .build())
                            .addComponent(Component.text()
                                    .append(Component.text("Def: "))
                                    .append(Component.text(def))
                                    .build())
                            .addComponent(Component.text()
                                    .append(Component.text("Speed: "))
                                    .append(Component.text(speed))
                                    .build())
                            .addComponent(Component.text()
                                    .append(Component.text("Org: "))
                                    .append(Component.text(org))
                                    .build())
                            .build());
                });

    }

    private void divTypeClick(InventoryPreClickEvent e) {
        ContinentalManagers.guiManager.openGUI(new TroopTypeSelectionGUI(divTypeHash, design, e.getSlot()), (CPlayer) e.getPlayer());
    }
}
