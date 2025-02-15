package org.drachens.dataClasses.Research;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.events.NewDay;
import org.drachens.events.research.ResearchCompletionEvent;
import org.drachens.events.research.ResearchStartEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ResearchCountry {
    private final HashSet<String> completedResearch = new HashSet<>();
    private final List<Building> researchCentersBuildings = new ArrayList<>();
    private final Modifier researchModifier;
    private ResearchOption current = null;
    private Payment researchCurrent;
    private Runnable onFinishResearch;
    private final Country c;

    public ResearchCountry(Country c) {
        this.c=c;
        researchModifier = new Modifier.create(Component.text("Research", NamedTextColor.BLUE), "research")
                .setDisplay(false)
                .build();
        c.addModifier(researchModifier);
    }

    public void newWeek(NewDay newDay) {
        if (current != null) {
            researchCurrent.remove(c.getResearchVault().getResearch());
            if (researchCurrent.getAmount() <= 0) {
                EventDispatcher.call(new ResearchCompletionEvent(c.getInstance(), c, current));
                if (onFinishResearch != null) {
                    onFinishResearch.run();
                    onFinishResearch = null;
                }
            }
        }
    }

    public void addResearchCenter(Building building) {
        if (building.getBuildTypes() != BuildingEnum.researchCenter) return;
        researchCentersBuildings.add(building);
    }

    public void removeResearchCenter(Building building) {
        if (building.getBuildTypes() != BuildingEnum.researchCenter) return;
        researchCentersBuildings.remove(building);
    }

    public boolean isResearching() {
        return current != null;
    }

    public ResearchOption getCurrentResearch() {
        return current;
    }

    public void setCurrentResearch(ResearchOption identifier) {
        current = identifier;
        researchCurrent = identifier.getCost();
    }

    public void completeActiveResearch() {
        completedResearch.add(current.getIdentifier());
        if (current.getModifier() != null) {
            researchModifier.addModifier(current.getModifier());
        }
        current = null;
        researchCurrent = null;
    }

    public List<String> getCompletedResearch() {
        return new ArrayList<>(completedResearch);
    }

    public boolean hasResearched(String identifier) {
        return completedResearch.contains(identifier);
    }

    public List<Building> getResearchCentersBuildings() {
        return researchCentersBuildings;
    }

    public boolean hasResearchedAny(List<String> researches) {
        for (String s : researches) {
            if (hasResearched(s)) return true;
        }
        return false;
    }

    public boolean hasResearchedAll(List<String> researches) {
        if (researches.isEmpty()) return true;
        for (String s : researches) {
            if (!hasResearched(s)) return false;
        }
        return true;
    }

    public void startResearching(ResearchOption researchOption) {
        EventDispatcher.call(new ResearchStartEvent(c.getInstance(), c, researchOption));
    }
}
