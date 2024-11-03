package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.dataClasses.Economics.BuildTypes;

import java.util.HashMap;

public class BuildingTypes {
    HashMap<String, BuildTypes> buildTypesHashMap = new HashMap<>();

    public void register(BuildTypes buildTypes){
        buildTypesHashMap.put(buildTypes.getIdentifier(), buildTypes);
    }
    public BuildTypes getBuildType(String name){
        return buildTypesHashMap.get(name);
    }
}
