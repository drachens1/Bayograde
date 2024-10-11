package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.dataClasses.Economics.factory.FactoryType;

import java.util.HashMap;

public class Placeables {
    HashMap<String, FactoryType> factoryTypeHashMap = new HashMap<>();

    public void register(FactoryType factoryType){
        this.factoryTypeHashMap.put(factoryType.getIdentifier(), factoryType);
    }

    public void unregister(FactoryType factoryType){
        factoryTypeHashMap.remove(factoryType.getIdentifier());
    }

    public FactoryType getFactoryType(String name){
        return factoryTypeHashMap.get(name);
    }

    public HashMap<String, FactoryType> getFactoryTypeHashMap(){
        return factoryTypeHashMap;
    }
}
