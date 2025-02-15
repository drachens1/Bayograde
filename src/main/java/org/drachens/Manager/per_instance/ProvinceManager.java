package org.drachens.Manager.per_instance;

import com.google.gson.JsonElement;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import org.drachens.dataClasses.FlatPos;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.Saveable;

import java.util.HashMap;
import java.util.Map;

public class ProvinceManager implements Saveable {
    private Map<FlatPos, Province> provinceHashMap;

    public ProvinceManager() {
        provinceHashMap = new HashMap<>();
    }

    public Province getProvince(Pos pos) {
        return provinceHashMap.get(new FlatPos((int) pos.x(), (int) pos.z()));
    }

    public Province getProvince(int x, int z) {
        return provinceHashMap.get(new FlatPos(x, z));
    }

    public Province getProvince(FlatPos flatPos) {
        return provinceHashMap.get(flatPos);
    }

    public Province getProvince(Point point) {
        return provinceHashMap.get(new FlatPos((int) point.x(), (int) point.z()));
    }

    public void registerProvince(int x, int z, Province province) {
        provinceHashMap.put(new FlatPos(x, z), province);
    }

    public void reset() {
        provinceHashMap = new HashMap<>();
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
