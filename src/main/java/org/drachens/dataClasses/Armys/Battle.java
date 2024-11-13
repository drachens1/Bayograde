package org.drachens.dataClasses.Armys;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;

import java.util.ArrayList;
import java.util.List;

public class Battle {
    private final Province province;
    private final List<Troop> attackers;

    public Battle(Province province, Troop attacker) {
        this.province = province;
        attackers = new ArrayList<>();
        attackers.add(attacker);
        province.getTroops().forEach((troop -> {
            if (troop.getBattle() == null)
                troop.joinBattle(this);
        }));
    }

    public Province getProvince() {
        return province;
    }

    public void addTroop(Troop troop) {
        this.attackers.add(troop);
    }

    public void removeTroop(Troop troop) {
        this.attackers.remove(troop);
    }

    public boolean containsTroop(Troop troop) {
        return this.attackers.contains(troop);
    }

    public void end(Country winner) {
        if (province.getOccupier() != winner)
            province.setOccupier(winner);
    }

    public void calculateWave() {

    }
}
