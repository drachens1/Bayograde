package org.drachens.dataClasses.Diplomacy;

import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relations {
    private final Country country;
    private final Map<Country, Float> relationsHashMap = new HashMap<>();
    private final Map<Country,Float> positiveRelations = new HashMap<>();
    private final Map<Country,Float> negativeRelations = new HashMap<>();
    public Relations(Country country){
        this.country = country;
    }
    public void increase(Country country, float amount){
        amount*=country.getRelationsBoost();
        if (!relationsHashMap.containsKey(country)){
            relationsHashMap.put(country,amount);
            return;
        }
        float newAmount = relationsHashMap.get(country)+amount;
        relationsHashMap.put(country,newAmount);
        if (newAmount>0){
            positiveRelations.put(country,newAmount);
            negativeRelations.remove(country);
        }
        if (newAmount<0){
            negativeRelations.put(country,newAmount);
            positiveRelations.remove(country);
        }
    }
    public Map<Country, Float> getNegativeRelations(){
        return negativeRelations;
    }
    public Map<Country,Float> getPositiveRelations(){
        return positiveRelations;
    }
}
