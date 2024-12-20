package org.drachens.dataClasses.Armys;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.InventorySystem.InventoryButton;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.interfaces.DivisionStatsCalculator;
import org.drachens.temporary.troops.TroopCountry;

import java.util.HashMap;
import java.util.List;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class DivisionDesign {
    private final Profile profile;
    private final TroopCountry country;
    private HashMap<Integer, DivisionType> design;
    private float hp;
    private float atk;
    private float def;
    private float speed;
    private HashMap<CurrencyTypes, Payment> paymentList;
    private String name;

    public DivisionDesign(String name, HashMap<Integer, DivisionType> design, DivisionStatsCalculator divisionStatsCalculator, TroopCountry country) {
        this.design = design;
        this.paymentList = new HashMap<>();
        this.hp = 1f;
        this.atk = 1f;
        this.def = 1f;
        this.speed = 1f;
        this.country = country;
        this.name = name;
        profile=new Profile(itemBuilder(Material.ORANGE_DYE,Component.text(name)));
    }

    public DivisionDesign(DivisionDesign design) {
        this.design = design.design;
        this.atk = design.atk;
        this.def = design.def;
        this.speed = design.speed;
        this.paymentList = design.paymentList;
        this.country = design.country;
        this.name = design.name;
        this.hp=design.hp;
        profile=new Profile(itemBuilder(Material.ORANGE_DYE,Component.text(design.name)));

    }

    public HashMap<Integer, DivisionType> getDesign() {
        return design;
    }

    public void setDesign(HashMap<Integer, DivisionType> design) {
        this.design = design;
    }

    public void addDesign(int slot, DivisionType divisionType) {
        this.design.put(slot, divisionType);
    }

    public DivisionType getDivisionType(int slot) {
        return design.get(slot);
    }

    public float getAtk() {
        return atk;
    }

    public void setAtk(float atk) {
        this.atk = atk;
    }

    public float getDef() {
        return def;
    }

    public void setDef(float def) {
        this.def = def;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public HashMap<CurrencyTypes, Payment> getCost() {
        return paymentList;
    }

    public void setCost(HashMap<CurrencyTypes, Payment> cost) {
        this.paymentList = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float calculateTime() {
        return design.size();
    }

    public TroopCountry getCountry() {
        return country;
    }

    public Profile getProfile(){
        return profile;
    }

    public static class Profile {
        private ItemStack face;
        private final InventoryButton train = new InventoryButton()
                .creator(player -> ItemStack.builder(Material.GREEN_STAINED_GLASS)
                        .customName(Component.text("Train"))
                        .build())
                .consumer(e -> {});
        private final InventoryButton edit = new InventoryButton()
                .creator(player -> ItemStack.builder(Material.YELLOW_STAINED_GLASS)
                        .customName(Component.text("Edit"))
                        .build())
                .consumer(e -> {});
        private final InventoryButton delete = new InventoryButton()
                .creator(player -> ItemStack.builder(Material.RED_STAINED_GLASS)
                        .customName(Component.text("Delete"))
                        .build())
                .consumer(e -> {});

        public Profile(ItemStack face){
            this.face=face;
        }
        public void rename(Component newName){
            face = face.withCustomName(newName);
        }
        public void setLore(List<Component> lore){
            face = face.withLore(lore);
        }
        public ItemStack getFace(){
            return face;
        }
        public InventoryButton getTrain(){
            return train;
        }
        public InventoryButton getEdit(){
            return edit;
        }
        public InventoryButton getDelete(){
            return delete;
        }
    }
}
