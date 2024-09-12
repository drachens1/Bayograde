package org.drachens.dataClasses.Countries;

import net.minestom.server.item.Material;
import net.minestom.server.scoreboard.Team;
import org.drachens.dataClasses.Economics.currency.Currencies;
import org.drachens.dataClasses.Economics.currency.CurrencyTypes;
import org.drachens.dataClasses.Provinces.Province;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Country extends Team {
    private List<Province> cores;
    private List<Province> occupies;
    private HashMap<CurrencyTypes, Currencies> currenciesMap;
    private String name;
    private Material block;
    private Material border;
    public Country(HashMap<CurrencyTypes,Currencies> startingCurrencies, String name, Material block, Material border){
        super(name);
        this.cores = new ArrayList<>();
        this.occupies = new ArrayList<>();
        this.currenciesMap = startingCurrencies;
        this.name = name;
        this.block = block;
        this.border = border;
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
    public void  setOccupies(List<Province> provinces){
        occupies = provinces;
    }
    public void setCores(List<Province> cores) {
        this.cores = cores;
    }
    public void add(CurrencyTypes currencyTypes, float amount){
        currenciesMap.get(currencyTypes).add(amount);
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Material getBlock(){
        return block;
    }
    public void setBlock(Material block) {
        this.block = block;
    }
    public Material getBorder(){
        return border;
    }
    public void setBorder(Material border){
        this.border = border;
    }
}
