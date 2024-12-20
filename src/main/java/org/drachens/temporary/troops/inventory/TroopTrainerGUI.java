package org.drachens.temporary.troops.inventory;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.InventorySystem.InventoryGUI;
import org.drachens.dataClasses.Armys.DivisionDesign;
import org.drachens.temporary.troops.TroopCountry;
import org.jetbrains.annotations.NotNull;

import static org.drachens.util.InventoryUtil.addExitButton;

public class TroopTrainerGUI extends InventoryGUI{
    @Override
    protected Inventory createInventory() {
        return new Inventory(InventoryType.CHEST_6_ROW,"Trainer");
    }

    @Override
    public void decorate(@NotNull CPlayer p) {
        TroopCountry country = (TroopCountry) p.getCountry();
        int x = 1;
        for (DivisionDesign divisionDesign : country.getDivisionDesigns()){
            DivisionDesign.Profile profile = divisionDesign.getProfile();
            addButton(x,head(profile));
            addButton(x+1,profile.getDelete());
            addButton(x+9,profile.getTrain());
            addButton(x+10,profile.getEdit());
            x+=2;
        }
        for (int i = 0; i < 54; i+=9){
            addButton(i,sideButtons());
        }
        addExitButton(this);
        super.decorate(p);
    }

    protected InventoryButton sideButtons() {
        return new InventoryButton()
                .creator(player -> ItemStack.builder(Material.GREEN_STAINED_GLASS_PANE)
                        .customName(Component.text(" "))
                        .build());
    }

    protected InventoryButton head(DivisionDesign.Profile profile){
        return new InventoryButton()
                .creator(player -> profile.getFace());
    }
}
