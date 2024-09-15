package org.drachens.dataClasses.Armys;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Troop {
    private final ItemDisplay troop;
    private ItemDisplay ally;
    private ItemDisplay enemey;
    private final Province province;

    public Troop(Province province, Material item, int modelData) {
        this.troop = new ItemDisplay(itemBuilder(item, modelData), province, ItemDisplay.DisplayType.GROUND, true);
        this.province = province;
    }

    public Troop(Province province, Material item, int troop, int ally, int enemy) {
        this.troop = new ItemDisplay(itemBuilder(item, troop), province, ItemDisplay.DisplayType.GROUND, true);
        this.ally = new ItemDisplay(itemBuilder(item, ally), province, ItemDisplay.DisplayType.GROUND, true);
        this.enemey = new ItemDisplay(itemBuilder(item, enemy), province, ItemDisplay.DisplayType.GROUND, true);
        this.province = province;
    }

    public Province getProvince() {
        return province;
    }

    public ItemDisplay getAlly() {
        return ally;
    }

    public ItemDisplay getEnemey() {
        return enemey;
    }

    public ItemDisplay getTroop() {
        return troop;
    }
}
