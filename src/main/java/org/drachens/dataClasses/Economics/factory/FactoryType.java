package org.drachens.dataClasses.Economics.factory;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;

import java.util.HashMap;
import java.util.List;

public class FactoryType {
    //The currencies it produces
    private final List<CurrencyTypes> currency;
    //The amount it costs
    private final List<Integer> produce;
    private final int[] modelData;
    private final Material item;
    //Max corrosponds to the material below
    private final HashMap<Material, Integer> max;
    public FactoryType(List<CurrencyTypes> currency, List<Integer> production, int[] modelData, Material item, HashMap<Material, Integer> max){
        this.currency = currency;
        this.produce = production;
        this.modelData = modelData;
        this.item = item;
        this.max = max;
    }
    public List<CurrencyTypes> getCurrency(){
        return currency;
    }
    public List<Integer> getProduction(){
        return produce;
    }
    public int[] getModelData(){
        return modelData;
    }
    public Material getItem(){
        return item;
    }
    public Integer getMax(Material material){
        return max.get(material);
    }
}
