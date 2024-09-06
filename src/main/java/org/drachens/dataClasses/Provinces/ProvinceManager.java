package org.drachens.dataClasses.Provinces;

import net.minestom.server.coordinate.Pos;

import java.util.HashMap;

public class ProvinceManager {
    private HashMap<Pos, Province> provinceHashMap;
    public ProvinceManager(){
        provinceHashMap = new HashMap<>();
    }

    public Province getProvince(Pos pos){
        return provinceHashMap.get(pos);
    }

    public Province getProvince(int x, int y, int z){
        return provinceHashMap.get(new Pos(x,y,z));
    }

    public void registerProvince(Pos pos, Province province){
        provinceHashMap.put(pos,province);
    }
}
