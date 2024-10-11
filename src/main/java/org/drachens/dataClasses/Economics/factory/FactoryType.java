package org.drachens.dataClasses.Economics.factory;

import net.kyori.adventure.text.Component;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FactoryType {
    //The currencies it produces
    private final List<CurrencyTypes> currency;
    //The amount it costs
    private final List<Float> produce;
    private final int[] modelData;
    private final Material item;
    private final Component name;
    //Max corrosponds to the material below
    private final HashMap<Material, Integer> max;
    private final List<Float> boosts;
    private final String identifier;

    public FactoryType(CurrencyTypes currency, Float production, int[] modelData, Material item, HashMap<Material, Integer> max, Component name, String identifier) {
        this.identifier = identifier;
        this.name = name;
        this.boosts = new ArrayList<>();
        this.currency = new ArrayList<>();
        this.currency.add(currency);
        this.produce = new ArrayList<>();
        this.produce.add(production);
        this.modelData = modelData;
        this.item = item;
        this.max = max;
    }

    public FactoryType(List<String> currency, List<Float> production, int[] modelData, Material item, HashMap<Material, Integer> max, Component name, String identifier) {
        this.identifier = identifier;
        this.name = name;
        this.currency = new ArrayList<>();
        Currencies currencies = ContinentalManagers.defaultsStorer.currencies;
        for (String s : currency){
            this.currency.add(currencies.getCurrencyType(s));
        }
        this.produce = production;
        this.modelData = modelData;
        this.item = item;
        this.max = max;
        this.boosts = new ArrayList<>();
    }

    public List<CurrencyTypes> getCurrency() {
        return currency;
    }

    public List<Float> getProduction() {
        return produce;
    }

    public int[] getModelData() {
        return modelData;
    }

    public Material getItem() {
        return item;
    }

    public Integer getMax(Material material) {
        return max.get(material);
    }

    public Component getName() {
        return name;
    }

    public String getIdentifier(){
        return identifier;
    }
}