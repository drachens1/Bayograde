package org.drachens.dataClasses.Armys;

import lombok.Getter;
import org.drachens.dataClasses.Province;

import java.util.List;

@Getter
public class Frontline {
    private final List<Province> provinces;
    private final List<Troop> troops;

    public Frontline(List<Province> provinces, List<Troop> troops){
        this.provinces=provinces;
        this.troops=troops;
    }

    public void addTroop(Troop troop){
        troops.add(troop);
    }

    public void removeTroop(Troop troop){
        troops.remove(troop);
    }

    public void addProvince(Province province){
        provinces.add(province);
    }

    public void removeProvince(Province province){
        provinces.remove(province);
    }

    public void distributeTroopsEvenly() {
        if (provinces.isEmpty() || troops.isEmpty()) {
            return;
        }

        int provinceCount = provinces.size();
        int index = 0;

        for (Troop troop : troops) {
            Province targetProvince = provinces.get(index % provinceCount);
            targetProvince.addTroop(troop);
            index++;
        }
    }

}
