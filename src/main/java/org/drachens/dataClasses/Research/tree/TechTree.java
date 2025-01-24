package org.drachens.dataClasses.Research.tree;

import net.kyori.adventure.text.Component;
import org.drachens.temporary.research.ResearchCountry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TechTree {
    private final HashMap<String, List<String>> researchAfterHashMap = new HashMap<>();
    private final List<ResearchCategory> researchCategories;
    private final Component name;
    private final List<String> noRequires = new ArrayList<>();
    private final HashMap<String, ResearchOption> identifierHashMap = new HashMap<>();

    public TechTree(Create create) {
        name = create.name;
        researchCategories = create.researchCategories;
        researchCategories.forEach(researchCategory -> researchCategory.getResearchOptionList().forEach(researchOption -> identifierHashMap.put(researchOption.getIdentifier(), researchOption)));
        for (ResearchCategory researchCategory : researchCategories) {
            for (ResearchOption researchOption : researchCategory.getResearchOptionList()) {
                List<String> requires = researchOption.getRequires();
                if (requires.isEmpty()) {
                    noRequires.add(researchOption.getIdentifier());
                } else {
                    requires.forEach(require -> {
                        List<String> s = researchAfterHashMap.getOrDefault(require, new ArrayList<>());
                        s.add(researchOption.getIdentifier());
                        researchAfterHashMap.put(require, s);
                    });
                }
            }
        }
    }

    public Component getName() {
        return name;
    }

    public List<ResearchCategory> getResearchCategories() {
        return researchCategories;
    }

    public ResearchOption getResearchOption(String identifier) {
        return identifierHashMap.get(identifier);
    }

    public List<String> getAfter(String identifier) {
        return researchAfterHashMap.getOrDefault(identifier, new ArrayList<>());
    }

    public List<String> getNoRequiresString() {
        return noRequires;
    }

    public List<String> getAvailable(ResearchCountry country) {
        List<String> available = getNoRequiresString();
        country.getCompletedResearch().forEach(completedResearch -> {
            available.remove(completedResearch);
            available.addAll(getAfter(completedResearch));
        });
        return available;
    }

    public static class Create {
        private final List<ResearchCategory> researchCategories = new ArrayList<>();
        private final Component name;

        public Create(Component name) {
            this.name = name;
        }

        public Create addCategory(ResearchCategory researchCategory) {
            researchCategories.add(researchCategory);
            return this;
        }

        public TechTree build() {
            return new TechTree(this);
        }
    }
}
