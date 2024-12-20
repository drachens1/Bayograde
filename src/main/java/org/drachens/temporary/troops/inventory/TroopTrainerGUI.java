package org.drachens.temporary.troops.inventory;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.temporary.troops.TroopCountry;
import org.drachens.temporary.troops.TroopStatsCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import static org.drachens.util.InventoryUtil.addExitButton;

public class TroopTrainerGUI extends InventoryGUI{
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW,"Trainer");
    }

    @Override
    public void decorate(@NotNull CPlayer p) {
        TroopCountry country = (TroopCountry) p.getCountry();
        int y = 0;
        int x = 1;
        for (DivisionDesign divisionDesign : country.getDivisionDesigns()){
            if (x>8){
                x=1;
                y+=18;
            }
            DivisionDesign.Profile profile = divisionDesign.getProfile();
            addButton(y+x,head(profile));
            addButton(y+x+1,profile.getDelete());
            addButton(y+x+9,profile.getTrain());
            addButton(y+x+10,profile.getEdit());
            x+=2;
        }
        if (country.getDivisionDesigns().size()<11) addButton(y+x+1,addNew());
        for (int i = 0; i < 54; i+=9){
            addButton(i,sideButtons());
        }
        addButton(52,sideButtons());
        addButton(43,sideButtons());
        addButton(44,sideButtons());
        addExitButton(this);
        super.decorate(p);
    }

    protected InventoryButton sideButtons() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.BLUE_STAINED_GLASS_PANE)
                        .customName(Component.text(" "))
                        .build());
    }

    protected InventoryButton addNew(){
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.GREEN_CONCRETE)
                        .customName(Component.text("Add new"))
                        .build())
                .consumer(inventoryPreClickEvent -> {
                    CPlayer p = (CPlayer) inventoryPreClickEvent.getPlayer();
                    TroopCountry troopCountry = (TroopCountry) p.getCountry();
                    troopCountry.addDivisionDesign(new DivisionDesign("Womp",new HashMap<>(),new TroopStatsCalculator(),troopCountry));
                    p.closeInventory();
                    ContinentalManagers.guiManager.openGUI(new TroopTrainerGUI(),p);
                });
    }

    protected InventoryButton head(DivisionDesign.Profile profile){
        return new InventoryButton()
                .creator(player -> profile.getFace());
    }
}
