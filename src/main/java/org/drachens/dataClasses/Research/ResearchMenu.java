package org.drachens.dataClasses.Research;

import org.drachens.interfaces.ResearchCategory;

import java.util.List;

public class ResearchMenu {
    private List<ResearchCategory> researchCategories;
    public ResearchMenu(List<ResearchCategory> researchCategories){
        this.researchCategories = researchCategories;
    }
}
