package org.drachens.dataClasses.Economics.defensive;

import net.minestom.server.item.Material;

public class DefensiveTypes {
    private final int maxLvl;
    private final float defenseBonus;
    private final int[] modelData;
    private final Material item;

    public DefensiveTypes(float defenseBonus, int maxLvl, int[] modelData, Material item) {
        this.maxLvl = maxLvl;
        this.defenseBonus = defenseBonus;
        this.modelData = modelData;
        this.item = item;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public float getDefenseBonus() {
        return defenseBonus;
    }

    public int[] getModelData() {
        return modelData;
    }

    public Material getItem() {
        return item;
    }
}
