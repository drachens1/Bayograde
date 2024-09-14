package org.drachens.dataClasses.Economics.factory;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.PlaceableBuilds;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.dataClasses.other.ItemDisplay;

import java.util.HashMap;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class PlaceableFactory implements PlaceableBuilds {
    private final ItemDisplay itemDisplay;
    private final FactoryType factoryType;
    private final Province province;
    private int current;
    private final int max;
    private final Country occupier;
    public PlaceableFactory(FactoryType factoryType, Province province){
        System.out.println("PLACED");
        this.factoryType = factoryType;
        this.province = province;
        this.current = 1;
        this.occupier = province.getOccupier();
        itemDisplay = new ItemDisplay(itemBuilder(factoryType.getItem(),factoryType.getModelData()[current]),province);
        this.max = factoryType.getMax(province.getMaterial());
    }
    private void updateDisplay(){
        itemDisplay.setItem(itemBuilder(factoryType.getItem(),factoryType.getModelData()[current]));
    }
    public boolean canAddFactory(int add){
        return current + add <= max;
    }

    @Override
    public void onCaptured(Country capturer) {
        occupier.removePlaceableFactory(this);
        capturer.addPlaceableFactory(this);
    }

    @Override
    public void onBombed(float dmg) {

    }

    @Override
    public void onDestroy() {
        itemDisplay.delete();
        province.setBuildType(null);
        occupier.removePlaceableFactory(this);
    }

    @Override
    public void onConstruct() {
        occupier.addPlaceableFactory(this);
    }

    @Override
    public void onUpgrade(int amount) {
        if (canAddFactory(amount)){
            return;
        }
        current+=amount;
        if (current <= 0){
            itemDisplay.delete();
        }
        updateDisplay();
    }

    public HashMap<CurrencyTypes, Float> onGenerate(){
        HashMap<CurrencyTypes, Float> currencyTypes = new HashMap<>();
        for (int i = 0; i < factoryType.getCurrency().size(); i++){
            currencyTypes.put(factoryType.getCurrency().get(i),factoryType.getProduction().get(i));
        }
        return currencyTypes;
    }
}
