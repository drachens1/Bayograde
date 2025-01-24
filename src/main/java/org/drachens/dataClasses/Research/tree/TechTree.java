package org.drachens.dataClasses.Research.tree;

import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Research.ResearchCategoryEnum;
import org.drachens.temporary.research.ResearchCountry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TechTree {
    private final HashMap<ResearchCategoryEnum, ResearchCategory> researchCategoriesHashMaps;
    private final HashMap<String, List<String>> researchAfterHashMap = new HashMap<>();
    private final List<ResearchCategory> researchCategories;
    private final Component name;
    private final List<String> noRequires = new ArrayList<>();
    private final HashMap<String, ResearchOption> identifierHashMap = new HashMap<>();
    private final HashMap<Integer[], ResearchOption> researchOptionMap = new HashMap<>();

    public TechTree(Create create) {
        name = create.name;
        researchCategoriesHashMaps = create.researchCategoriesHashmap;
        researchCategories = create.researchCategories;
        researchCategories.forEach(researchCategory -> researchCategory.getResearchOptionList().forEach(researchOption -> identifierHashMap.put(researchOption.getIdentifier(), researchOption)));
        int i = 0;
        for (ResearchCategory researchCategory : researchCategories) {
            int[] last = new int[]{0, 0};
            for (ResearchOption researchOption : researchCategory.getResearchOptionList()) {
                if (researchOption.getComparedToLast() == null) {
                    researchOptionMap.put(new Integer[]{i + last[0], last[1]}, researchOption);
                    continue;
                }
                int[] a = researchOption.getComparedToLast();
                last = new int[]{last[0] + a[0], last[1] + a[1]};
                researchOptionMap.put(new Integer[]{i + last[0], last[1]}, researchOption);
            }
            i += 3;
        }

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

    public ResearchCategory getResearchCategory(ResearchCategoryEnum identifier) {
        return researchCategoriesHashMaps.get(identifier);
    }

    public ResearchOption getResearchOption(String identifier) {
        return identifierHashMap.get(identifier);
    }

    public boolean isResearchOptionCancelled(String identifier) {
        return identifierHashMap.containsKey(identifier);
    }

    public HashMap<Integer[], ResearchOption> getResearchOptionMap() {
        return researchOptionMap;
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
        private final HashMap<ResearchCategoryEnum, ResearchCategory> researchCategoriesHashmap = new HashMap<>();
        private final List<ResearchCategory> researchCategories = new ArrayList<>();
        private final Component name;

        public Create(Component name) {
            this.name = name;
        }

        public Create addCategory(ResearchCategory researchCategory) {
            researchCategoriesHashmap.put(researchCategory.getIdentifier(), researchCategory);
            researchCategories.add(researchCategory);
            return this;
        }

        public TechTree build() {
            return new TechTree(this);
        }
    }
}
