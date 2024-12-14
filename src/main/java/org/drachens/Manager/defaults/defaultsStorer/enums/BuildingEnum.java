package org.drachens.Manager.defaults.defaultsStorer.enums;

import org.drachens.dataClasses.Economics.BuildTypes;
import org.drachens.dataClasses.Research.ResearchCenter;
import org.drachens.temporary.Factory;
import org.drachens.temporary.research.ResearchLab;
import org.drachens.temporary.research.ResearchLibrary;
import org.drachens.temporary.research.ResearchUniversity;

public enum BuildingEnum {
    university(new ResearchUniversity()),
    library(new ResearchLibrary()),
    factory(new Factory()),
    researchCenter(new ResearchCenter()),
    researchLab(new ResearchLab()),
    research(null);

    private final BuildTypes buildTypes;
    BuildingEnum(BuildTypes buildTypes){
        this.buildTypes=buildTypes;
    }
    public BuildTypes getBuildTypes(){
        return buildTypes;
    }
}
