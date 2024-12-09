package org.drachens.temporary.research;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.Material;
import org.drachens.Manager.defaults.defaultsStorer.enums.BuildingEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.Election;
import org.drachens.dataClasses.Countries.Ideology;
import org.drachens.dataClasses.Economics.Building;
import org.drachens.dataClasses.Economics.Vault;
import org.drachens.dataClasses.Economics.currency.Payment;
import org.drachens.dataClasses.Modifier;
import org.drachens.dataClasses.Research.tree.ResearchOption;
import org.drachens.events.NewDay;
import org.drachens.events.research.ResearchCompletionEvent;
import org.drachens.temporary.clicks.ClicksVault;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public abstract class ResearchCountry extends Country {
    private final HashSet<String> completedResearch = new HashSet<>();
    private ResearchOption current = null;
    private Payment researchCurrent;
    private final List<Building> researchCentersBuildings = new ArrayList<>();
    private final Modifier researchModifier;

    public ResearchCountry(String name, Component nameComponent, Material block, Material border, Ideology defaultIdeologies, Election election, Instance instance, Vault vault) {
        super(name, nameComponent, block, border, defaultIdeologies, election, instance, vault);
        researchModifier=new Modifier.create(Component.text("Research", NamedTextColor.BLUE))
                .setDisplay(false)
                .build();
        addModifier(researchModifier);
    }

    @Override
    public void newWeek(NewDay newDay) {
        if (current!=null){
            ClicksVault vault = (ClicksVault) getVault();
            researchCurrent.remove(vault.getResearch());
            if (researchCurrent.getAmount()<=0){
                completeActiveResearch();
            }
        }
    }

    public void addResearchCenter(Building building){
        if (building.getBuildTypes()!= BuildingEnum.researchCenter)return;
        researchCentersBuildings.add(building);
    }

    public void removeResearchCenter(Building building){
        if (building.getBuildTypes()!=BuildingEnum.researchCenter)return;
        researchCentersBuildings.remove(building);
    }

    public void setCurrentResearch(ResearchOption identifier){
        current=identifier;
        researchCurrent=identifier.getCost();
    }

    public boolean isResearching(){
        return current!=null;
    }

    public ResearchOption getCurrentResearch(){
        return current;
    }

    public void completeActiveResearch(){
        EventDispatcher.call(new ResearchCompletionEvent(getInstance(),this,current));
        completedResearch.add(current.getIdentifier());
        if (current.getModifier()!=null){
            researchModifier.addModifier(current.getModifier());
        }
        current=null;
        researchCurrent=null;
    }

    public boolean hasResearched(String identifier){
        return completedResearch.contains(identifier);
    }

    public List<Building> getResearchCentersBuildings(){
        return researchCentersBuildings;
    }

    public boolean hasResearchedAny(List<String> researches){
        for (String s : researches){
            if (hasResearched(s))return true;
        }
        return false;
    }
    public boolean hasResearchedAll(List<String> researches){
        if (researches.isEmpty())return true;
        for (String s : researches){
            if (!hasResearched(s))return false;
        }
        return true;
    }

}
