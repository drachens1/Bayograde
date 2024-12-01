package org.drachens.Manager.defaults.defaultsStorer;

import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.dataClasses.Economics.BuildTypes;

import java.util.HashMap;

public class BuildingTypes {
    HashMap<BuildingEnum, BuildTypes> buildTypesHashMap = new HashMap<>();

    public void register(BuildTypes buildTypes) {
        buildTypesHashMap.put(buildTypes.getIdentifier(), buildTypes);
    }

    public BuildTypes getBuildType(BuildingEnum buildingEnum){
        return buildTypesHashMap.get(buildingEnum);
    }
}
