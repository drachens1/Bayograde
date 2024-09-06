package org.drachens.dataClasses.Economics.defensive;

import org.drachens.dataClasses.Economics.PlaceableBuilds;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class PlaceableDefensive implements PlaceableBuilds {
    private final ItemDisplay itemDisplay;
    private final DefensiveTypes DefensiveTypes;
    private final Province province;
    private int current;
    private int max;
    public PlaceableDefensive(DefensiveTypes DefensiveTypes, Province province){
        this.DefensiveTypes = DefensiveTypes;
        this.province = province;
        this.current = 1;
        itemDisplay = new ItemDisplay(itemBuilder(DefensiveTypes.getItem(),DefensiveTypes.getModelData()[current]),province);
        this.max = DefensiveTypes.getMaxLvl();
        province.setBuildType(this);
    }

    @Override
    public void onDestroy() {
        itemDisplay.delete();
    }
}
