package org.drachens.Manager.per_instance;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import org.drachens.dataClasses.Province;

import java.util.HashMap;
import java.util.Map;

public class ProvinceManager {
    private Map<Pos, Province> provinceHashMap;

    public ProvinceManager() {
        provinceHashMap = new HashMap<>();
    }

    public Province getProvince(Pos pos) {
        return provinceHashMap.get(pos);
    }

    public Province getProvince(int x, int y, int z) {
        return provinceHashMap.get(new Pos(x, y, z));
    }

    public Province getProvince(Point point) {
        return provinceHashMap.get(new Pos(point));
    }

    public void registerProvince(Pos pos, Province province) {
        provinceHashMap.put(pos, province);
    }

    public Map<Pos, Province> getProvinceHashMap() {
        return provinceHashMap;
    }

    public void reset() {
        provinceHashMap = new HashMap<>();
    }
}
