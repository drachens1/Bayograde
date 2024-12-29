package org.drachens.Manager.per_instance;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import org.drachens.dataClasses.FlatPos;
import org.drachens.dataClasses.territories.Province;

import java.util.HashMap;
import java.util.Map;

public class ProvinceManager {
    private Map<FlatPos, Province> provinceHashMap;

    public ProvinceManager() {
        provinceHashMap = new HashMap<>();
    }

    public Province getProvince(Pos pos) {
        return provinceHashMap.get(new FlatPos((int) pos.x(), (int) pos.z()));
    }

    public Province getProvince(int x, int z) {
        return provinceHashMap.get(new FlatPos(x,z));
    }

    public Province getProvince(FlatPos flatPos){
        return provinceHashMap.get(flatPos);
    }

    public Province getProvince(Point point) {
        return provinceHashMap.get(new FlatPos((int) point.x(), (int) point.z()));
    }

    public void registerProvince(int x, int z, Province province) {
        provinceHashMap.put(new FlatPos(x,z), province);
    }

    public Map<FlatPos, Province> getProvinceHashMap() {
        return provinceHashMap;
    }

    public void reset() {
        provinceHashMap = new HashMap<>();
    }
}
