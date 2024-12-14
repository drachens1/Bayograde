package org.drachens.Manager.defaults.defaultsStorer.enums;

import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.temporary.Factory;
import org.drachens.temporary.research.ResearchLab;
import org.drachens.temporary.research.ResearchLibrary;
import org.drachens.temporary.research.ResearchUniversity;

import java.util.HashSet;
import java.util.List;

public enum BuildingEnum {
    university(new ResearchUniversity(),new HashSet<>(List.of("research"))),
    library(new ResearchLibrary(),new HashSet<>(List.of("research"))),
    factory(new Factory(),new HashSet<>(List.of("fac"))),
    researchCenter(new ResearchCenter(),new HashSet<>(List.of("research"))),
    researchLab(new ResearchLab(),new HashSet<>(List.of("research")));

    private final HashSet<String> synonyms;
    private final BuildTypes buildTypes;
    BuildingEnum(BuildTypes buildTypes,HashSet<String> synonyms){
        this.synonyms=synonyms;
        this.buildTypes=buildTypes;
    }
    public BuildTypes getBuildTypes(){
        return buildTypes;
    }
    public HashSet<String> getSynonyms(){
        return synonyms;
    }
}
