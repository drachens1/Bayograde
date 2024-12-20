package org.drachens.Manager.defaults.defaultsStorer.enums;

import org.drachens.dataClasses.Economics.BuildTypes;

import java.util.HashSet;
import java.util.List;

public enum BuildingEnum {
    university(new HashSet<>(List.of("research"))),
    library(new HashSet<>(List.of("research"))),
    factory(new HashSet<>(List.of("economy"))),
    researchCenter(new HashSet<>(List.of("research"))),
    researchLab(new HashSet<>(List.of("research"))),
    barracks(new HashSet<>(List.of("military")));

    private final HashSet<String> synonyms;
    private BuildTypes buildTypes;

    BuildingEnum(HashSet<String> synonyms) {
        this.synonyms = synonyms;
    }

    public void setBuildType(BuildTypes buildTypes){
        this.buildTypes=buildTypes;
    }

    public BuildTypes getBuildTypes() {
        return buildTypes;
    }

    public HashSet<String> getSynonyms() {
        return synonyms;
    }
}
