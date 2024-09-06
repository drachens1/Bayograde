package org.drachens.dataClasses.Economics.factory;

import org.drachens.dataClasses.Economics.PlaceableBuilds;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class PlaceableFactory implements PlaceableBuilds {
    private final ItemDisplay itemDisplay;
    private final FactoryType factoryType;
    private final Province province;
    private int current;
    private int max;
    public PlaceableFactory(FactoryType factoryType, Province province){
        this.factoryType = factoryType;
        this.province = province;
        this.current = 1;
        itemDisplay = new ItemDisplay(itemBuilder(factoryType.getItem(),factoryType.getModelData()[current]),province);
        this.max = factoryType.getMax(province.getMaterial());
        province.setBuildType(this);
    }
    private void updateDisplay(){
        itemDisplay.setItem(itemBuilder(factoryType.getItem(),factoryType.getModelData()[current]));
    }
    public void addFactory(int add){
        if (canAddFactory(add)){
            return;
        }
        current+=add;
        if (current <= 0){
            itemDisplay.delete();
        }
        updateDisplay();
    }
    public boolean canAddFactory(int add){
        return current + add <= max;
    }

    @Override
    public void onDestroy() {
        itemDisplay.delete();
    }
}
