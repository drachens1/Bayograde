package org.drachens.dataClasses.Armys;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Economics.currency.Payments;
import org.drachens.generalGame.troops.TroopCountry;
import org.drachens.generalGame.troops.inventory.TroopEditGUI;
import org.drachens.interfaces.Saveable;
import org.drachens.player_types.CPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.drachens.util.ItemStackUtil.itemBuilder;

@Getter
@Setter
public class DivisionDesign implements Saveable {
    private final Profile profile;
    private final TroopCountry country;
    private HashMap<Integer, DivisionType> design;
    private float hp;
    private float atk;
    private float def;
    private float speed;
    private float org;
    private Payments paymentList;
    private String name;

    public DivisionDesign(String name, HashMap<Integer, DivisionType> design, TroopCountry country) {
        this.design = design;
        this.paymentList = new Payments();
        this.hp = 0.0f;
        this.atk = 0.0f;
        this.def = 0.0f;
        this.speed = 0.0f;
        this.org = 0.0f;
        this.country = country;
        this.name = name;
        profile = new Profile(itemBuilder(Material.ORANGE_DYE, Component.text(name)), this);
        calculate();
    }

    public DivisionDesign(DivisionDesign design) {
        this.design = design.design;
        this.atk = design.atk;
        this.def = design.def;
        this.speed = design.speed;
        this.paymentList = design.paymentList;
        this.country = design.country;
        this.name = design.name;
        this.hp = design.hp;
        this.org = design.org;
        profile = new Profile(itemBuilder(Material.ORANGE_DYE, Component.text(design.name)), this);

    }

    public void calculate() {
        this.hp = 0.0f;
        this.atk = 0.0f;
        this.def = 0.0f;
        this.speed = 0.0f;
        this.org = 0.0f;
        paymentList = new Payments();
        for (Map.Entry<Integer, DivisionType> e : design.entrySet()) {
            DivisionType d = e.getValue();
            hp += d.getHp();
            atk += d.getAtk();
            def += d.getDef();
            speed += d.getSpeed();
            org += d.getOrg();
            paymentList.addPayment(d.getCost());
        }
        paymentList.compress();
    }
    public void setDesign(HashMap<Integer, DivisionType> design) {
        this.design = design;
        calculate();
    }

    public void addDesign(int slot, DivisionType divisionType) {
        this.design.put(slot, divisionType);
    }

    public DivisionType getDivisionType(int slot) {
        return design.get(slot);
    }

    public Payments getCost() {
        return paymentList;
    }

    @Override
    public JsonElement toJson() {
        return null;
    }

    @Getter
    public static class Profile {
        private final InventoryButton train = new InventoryButton()
                .creator(player -> ItemStack.builder(Material.GREEN_STAINED_GLASS)
                        .customName(Component.text("Train"))
                        .build())
                .consumer(e -> {

                });
        private final InventoryButton edit;
        private final InventoryButton delete;
        private ItemStack face;

        public Profile(ItemStack face, DivisionDesign design) {
            this.face = face;
            this.edit = new InventoryButton()
                    .creator(player -> ItemStack.builder(Material.YELLOW_STAINED_GLASS)
                            .customName(Component.text("Edit"))
                            .build())
                    .consumer(e -> ContinentalManagers.guiManager.openGUI(new TroopEditGUI(new HashMap<>(design.getDesign()), design), (CPlayer) e.getPlayer()));
            delete = new InventoryButton()
                    .creator(player -> ItemStack.builder(Material.RED_STAINED_GLASS)
                            .customName(Component.text("Delete"))
                            .build());
        }

        public void rename(Component newName) {
            face = face.withCustomName(newName);
        }

        public void setLore(List<Component> lore) {
            face = face.withLore(lore);
        }

    }
}
