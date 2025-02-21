package org.drachens.dataClasses.Armys;

import com.google.gson.JsonElement;
import lombok.Getter;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.Saveable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ArmyGroup implements Saveable {
    private final int group;
    private final List<Troop> troops = new ArrayList<>();
    private final List<Province> troopProvinces = new ArrayList<>();

    public ArmyGroup(int group) {
        this.group = group;
    }

    public void addTroopProvinces(Province province) {
        this.troopProvinces.add(province);
    }

    public void removeTroopProvinces(Province province) {
        this.troopProvinces.remove(province);
    }

    public void addTroop(Troop troop) {
        this.troops.add(troop);
    }

    public void removeTroop(Troop troop) {
        this.troops.remove(troop);
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
