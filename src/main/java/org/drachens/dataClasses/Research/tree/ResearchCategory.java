package org.drachens.dataClasses.Research.tree;

import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Research.ResearchCategoryEnum;

import java.util.ArrayList;
import java.util.List;

public class ResearchCategory {
    private final ResearchCategoryEnum researchCategoryEnum;
    private final Component description;
    private final Component type;
    private final List<ResearchOption> researchOptionList;
    protected ResearchCategory(Create create){
        this.description=create.description;
        this.researchCategoryEnum=create.researchCategoryEnum;
        this.type=create.type;
        this.researchOptionList=create.researchOptionList;
        researchOptionList.forEach(researchOption -> researchOption.setResearchCategory(this));
    }
    public Component getDescription(){
        return description;
    }
    public Component getType(){
        return type;
    }
    public List<ResearchOption> getResearchOptionList(){
        return researchOptionList;
    }
    public ResearchCategoryEnum getIdentifier(){
        return researchCategoryEnum;
    }
    public static class Create {
        private final ResearchCategoryEnum researchCategoryEnum;
        private final Component description;
        private final Component type;
        private final List<ResearchOption> researchOptionList = new ArrayList<>();
        public Create(ResearchCategoryEnum researchCategoryEnum,Component description,Component type){
            this.researchCategoryEnum=researchCategoryEnum;
            this.description=description;
            this.type=type;
        }
        public Create addResearchOption(ResearchOption researchOption){
            researchOptionList.add(researchOption);
            return this;
        }
        public ResearchCategory build(){
            return new ResearchCategory(this);
        }
    }
}
