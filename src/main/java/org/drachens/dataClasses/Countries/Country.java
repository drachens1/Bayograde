package org.drachens.dataClasses.Countries;

import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Provinces.Province;

import java.util.HashMap;
import java.util.List;

public class Country {
    private List<Province> cores;
    private List<Province> occupies;
    private HashMap<CurrencyTypes, Currencies> currenciesMap;
    public Country(List<Province> cores, List<Province> occupies, HashMap<CurrencyTypes,Currencies> startingCurrencies){
        this.cores = cores;
        this.occupies = occupies;
        this.currenciesMap = startingCurrencies;
    }
    public List<Province> getCores(){
        return cores;
    }
    public void addCore(Province prov){
        cores.add(prov);
        prov.addCore(this);
    }
    public void removeCore(Province prov){
        cores.remove(prov);
        prov.removeCore(this);
    }
    public List<Province> getOccupies(){
        return occupies;
    }
    public void addOccupied(Province province){
        occupies.add(province);
    }
    public void removeOccupied(Province province){
        occupies.remove(province);
    }
    public void add(CurrencyTypes currencyTypes, float amount){
        currenciesMap.get(currencyTypes).add(amount);
    }
}
