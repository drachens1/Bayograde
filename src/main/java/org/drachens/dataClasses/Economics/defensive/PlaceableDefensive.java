package org.drachens.dataClasses.Economics.defensive;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.PlaceableBuilds;
import org.drachens.dataClasses.Economics.warBuild;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class PlaceableDefensive implements PlaceableBuilds, warBuild {
    private final ItemDisplay itemDisplay;
    private final DefensiveTypes DefensiveTypes;
    private final Province province;
    private final int current;
    private final int max;

    public PlaceableDefensive(DefensiveTypes DefensiveTypes, Province province) {
        this.DefensiveTypes = DefensiveTypes;
        this.province = province;
        this.current = 1;
        itemDisplay = new ItemDisplay(itemBuilder(DefensiveTypes.getItem(), DefensiveTypes.getModelData()[current]), province, ItemDisplay.DisplayType.GROUND, true);
        this.max = DefensiveTypes.getMaxLvl();
        province.setBuildType(this);
    }

    @Override
    public void onCaptured(Country capturer) {

    }

    @Override
    public void onBombed(float dmg) {

    }

    @Override
    public void onDestroy() {
        itemDisplay.delete();
    }

    @Override
    public void onUpgrade(int amount) {

    }

    @Override
    public void onClick() {

    }
}
