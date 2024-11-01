package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.dataClasses.BuildTypes;

import java.util.HashMap;

public class BuildingTypes {
    HashMap<String, BuildTypes> buildTypesHashMap = new HashMap<>();

    public void register(BuildTypes buildTypes){
        buildTypesHashMap.put(buildTypes.getIdentifier(), buildTypes);
    }

    public void unregister(BuildTypes buildTypes){
        buildTypesHashMap.remove(buildTypes.getIdentifier());
    }

    public BuildTypes getBuildType(String name){
        return buildTypesHashMap.get(name);
    }
    public HashMap<String, BuildTypes> getBuildTypesHashMap(){
        return buildTypesHashMap;
    }
}
