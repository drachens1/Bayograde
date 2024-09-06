package org.drachens.dataClasses.Armys;

import org.drachens.dataClasses.Provinces.Province;

import java.util.ArrayList;
import java.util.List;

public class ArmyGroup {
    private int group;
    private List<Troop> troops = new ArrayList<>();
    private List<Province> troopProvinces = new ArrayList<>();
    public ArmyGroup(int group){
        this.group = group;
    }
    public List<Province> getTroopProvinces() {
        return troopProvinces;
    }
    public void addTroopProvinces(Province province){
        this.troopProvinces.add(province);
    }
    public void removeTroopProvinces(Province province){
        this.troopProvinces.remove(province);
    }
    public List<Troop> getTroops() {
        return troops;
    }
    public void addTroop(Troop troop){
        this.troops.add(troop);
    }
    public void removeTroop(Troop troop){
        this.troops.remove(troop);
    }
}
