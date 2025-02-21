package org.drachens.generalGame.troops.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.generalGame.troops.buildings.Barracks;
import org.drachens.player_types.CPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static org.drachens.util.InventoryUtil.addExitButton;

public class TroopTrainerGUI extends InventoryGUI {
    private final Building building;

    public TroopTrainerGUI(Building building) {
        this.building = building;
    }

    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW, "Trainer");
    }

    @Override
    public void decorate(@NotNull CPlayer p) {
        TroopCountry country = (TroopCountry) p.getCountry();
        int y = 0;
        int x = 1;
        for (DivisionDesign divisionDesign : country.getDivisionDesigns()) {
            if (8 < x) {
                x = 1;
                y += 18;
            }
            DivisionDesign.Profile profile = divisionDesign.getProfile();
            addButton(y + x, head(profile));
            addButton(y + x + 1, profile.getDelete().consumer(e -> {
                TroopCountry country1 = (TroopCountry) p.getCountry();
                country1.removeDivisionDesign(divisionDesign);
                ContinentalManagers.guiManager.openGUI(new TroopTrainerGUI(building), p);
            }));
            addButton(y + x + 9, profile.getTrain().consumer(inventoryPreClickEvent -> {
                Barracks barracks = (Barracks) building.getBuildTypes().getBuildTypes();
                barracks.startTraining(building, divisionDesign, p);
            }));
            addButton(y + x + 10, profile.getEdit());
            x += 2;
        }
        if (11 > country.getDivisionDesigns().size()) addButton(y + x + 1, addNew());
        for (int i = 0; 54 > i; i += 9) {
            addButton(i, sideButtons());
        }
        addButton(52, sideButtons());
        addButton(43, sideButtons());
        addButton(44, sideButtons());
        addExitButton(this);
        super.decorate(p);
    }

    protected InventoryButton sideButtons() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.BLUE_STAINED_GLASS_PANE)
                        .customName(Component.text(" "))
                        .build());
    }

    protected InventoryButton addNew() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.GREEN_CONCRETE)
                        .customName(Component.text("Add new"))
                        .build())
                .consumer(inventoryPreClickEvent -> {
                    CPlayer p = (CPlayer) inventoryPreClickEvent.getPlayer();
                    TroopCountry troopCountry = (TroopCountry) p.getCountry();
                    troopCountry.addDivisionDesign(new DivisionDesign("Womp", new HashMap<>(), troopCountry));
                    p.closeInventory();
                    ContinentalManagers.guiManager.openGUI(new TroopTrainerGUI(building), p);
                });
    }

    protected InventoryButton head(DivisionDesign.Profile profile) {
        return new InventoryButton()
                .creator(player -> profile.getFace());
    }
}
