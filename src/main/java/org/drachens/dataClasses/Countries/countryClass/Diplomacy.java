package org.drachens.dataClasses.Countries.countryClass;

import lombok.Getter;
import lombok.Setter;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.Manager.defaults.enums.InvitesEnum;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.dataClasses.Province;

import java.util.*;

@Getter
@Setter
public class Diplomacy {
    private final List<Country> puppets;
    private final HashSet<String> countryWars;
    //1 = war 2 = neutral 3 = eco ally 4 = non aggression 5 = puppet/overlord 6 = mil ally
    private final HashMap<String, Integer> diplomacy;
    private final HashMap<String, NonAggressionPact> nonAggressionPactHashMap;
    private final HashMap<String, WarJustification> warJustificationHashMap;
    private final HashMap<String, WarJustification> completedWarJustifications;
    private final HashMap<String, List<Province>> bordersProvince;
    private final HashSet<String> bordersWars;
    private final HashMap<InvitesEnum, HashMap<String, Object>> invitesHashMap;
    private final HashMap<String, Demand> demandHashMap;
    private final HashMap<String, Demand> outgoingDemands;
    private final HashSet<ConditionEnum> conditionEnums;
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public Diplomacy(List<Country> puppets, HashSet<String> countryWars, HashMap<String, Integer> diplomacy,
                     HashMap<String, NonAggressionPact> nonAggressionPactHashMap,
                     HashMap<String, WarJustification> warJustificationHashMap,
                     HashMap<String, WarJustification> completedWarJustifications,
                     HashMap<String, List<Province>> bordersProvince, HashSet<String> bordersWars,
                     HashMap<InvitesEnum, HashMap<String, Object>> invitesHashMap,
                     HashMap<String, Demand> demandHashMap, HashMap<String, Demand> outgoingDemands,
                     HashSet<ConditionEnum> conditionEnums, Country country) {
        this.puppets = puppets;
        this.countryWars = countryWars;
        this.diplomacy = diplomacy;
        this.nonAggressionPactHashMap = nonAggressionPactHashMap;
        this.warJustificationHashMap = warJustificationHashMap;
        this.completedWarJustifications = completedWarJustifications;
        this.bordersProvince = bordersProvince;
        this.bordersWars = bordersWars;
        this.invitesHashMap = invitesHashMap;
        this.demandHashMap = demandHashMap;
        this.outgoingDemands = outgoingDemands;
        this.conditionEnums = conditionEnums;
    }

    public void addPuppet(Country country) {
        if (!puppets.contains(country)) {
            puppets.add(country);
        }
    }

    public void removePuppet(Country country) {
        puppets.remove(country);
    }

    public boolean containsPuppet(Country country) {
        return puppets.contains(country);
    }

    public void addCountryWar(String war) {
        countryWars.add(war);
    }

    public void removeCountryWar(String war) {
        countryWars.remove(war);
    }

    public boolean containsCountryWar(String war) {
        return countryWars.contains(war);
    }

    public Integer getDiplomaticRelation(String country) {
        return diplomacy.getOrDefault(country,2);
    }

    public void addDiplomaticRelation(String country, int value) {
        diplomacy.put(country, value);
    }

    public void removeDiplomaticRelation(String country) {
        diplomacy.remove(country);
    }

    public boolean containsDiplomaticRelation(String country) {
        return diplomacy.containsKey(country);
    }

    public NonAggressionPact getNonAggressionPact(String country) {
        return nonAggressionPactHashMap.get(country);
    }

    public void addNonAggressionPact(String other, NonAggressionPact pact) {
        nonAggressionPactHashMap.put(other, pact);
    }

    public void removeNonAggressionPact(String other) {
        nonAggressionPactHashMap.remove(other);
    }

    public boolean containsNonAggressionPact(String country) {
        return nonAggressionPactHashMap.containsKey(country);
    }

    public Set<String> getWarJustificationCountries(){
        return warJustificationHashMap.keySet();
    }

    public WarJustification getWarJustification(String country) {
        return warJustificationHashMap.get(country);
    }

    public void addWarJustification(String country, WarJustification justification) {
        warJustificationHashMap.put(country, justification);
    }

    public void removeWarJustification(String country) {
        warJustificationHashMap.remove(country);
    }

    public boolean containsWarJustification(String country) {
        return warJustificationHashMap.containsKey(country);
    }

    public WarJustification getCompletedWarJustification(String country) {
        return completedWarJustifications.get(country);
    }

    public void addCompletedWarJustification(String country, WarJustification justification) {
        completedWarJustifications.put(country, justification);
    }

    public void removeCompletedWarJustification(String country) {
        completedWarJustifications.remove(country);
    }

    public boolean containsCompletedWarJustification(String country) {
        return completedWarJustifications.containsKey(country);
    }

    public List<Province> getBorderProvinces(String country) {
        return bordersProvince.getOrDefault(country,new ArrayList<>());
    }

    public void addBorderProvince(String country, Province province) {
        bordersProvince.computeIfAbsent(country, k -> new ArrayList<>()).add(province);
    }

    public void removeBorderProvince(String country) {
        bordersProvince.remove(country);
    }

    public void removeBorderProvince(String country, Province province) {
        bordersProvince.computeIfPresent(country, (key, provinces) -> {
            provinces.remove(province);
            return provinces.isEmpty() ? null : provinces;
        });
    }

    public boolean containsBorderProvince(String country) {
        return bordersProvince.containsKey(country);
    }

    public void addBorderWar(String war) {
        bordersWars.add(war);
    }

    public void removeBorderWar(String war) {
        bordersWars.remove(war);
    }

    public boolean containsBorderWar(String war) {
        return bordersWars.contains(war);
    }

    public Set<String> getInviteKeys(InvitesEnum invitesEnum){
        return invitesHashMap.get(invitesEnum).keySet();
    }

    public Object getInvite(InvitesEnum inviteType, String key) {
        return invitesHashMap.containsKey(inviteType) ? invitesHashMap.get(inviteType).get(key) : null;
    }

    public void addInvite(InvitesEnum inviteType, String string, Object value) {
        invitesHashMap.computeIfAbsent(inviteType, k -> new HashMap<>()).put(string, value);
    }

    public void removeInvite(InvitesEnum inviteType, String string) {
        invitesHashMap.computeIfPresent(inviteType, (key, invites) -> {
            invites.remove(string);
            return invites.isEmpty() ? null : invites;
        });
    }

    public boolean containsInvite(InvitesEnum inviteType, String country) {
        return invitesHashMap.containsKey(inviteType) && invitesHashMap.get(inviteType).containsKey(country);
    }

    public boolean hasAnyDemands(){
        return getDemandCountries().isEmpty();
    }

    public Set<String> getDemandCountries(){
        return demandHashMap.keySet();
    }

    public Demand getDemand(String country){
        return demandHashMap.get(country);
    }

    public void addDemand(String country, Demand demand) {
        demandHashMap.put(country, demand);
    }

    public void removeDemand(String country) {
        demandHashMap.remove(country);
    }

    public boolean containsDemand(String country) {
        return demandHashMap.containsKey(country);
    }

    public Set<String> getOutgoingDemands(){
        return outgoingDemands.keySet();
    }

    public boolean hasOutgoingDemands(){
        return getOutgoingDemands().isEmpty();
    }

    public Demand getOutgoingDemand(String country) {
        return outgoingDemands.get(country);
    }

    public void addOutgoingDemand(String country, Demand demand) {
        outgoingDemands.put(country, demand);
    }

    public void removeOutgoingDemand(String country) {
        outgoingDemands.remove(country);
    }

    public boolean containsOutgoingDemand(String country) {
        return outgoingDemands.containsKey(country);
    }

    public boolean hasCondition(ConditionEnum conditionEnum){
        return conditionEnums.contains(conditionEnum);
    }

    public void addCondition(ConditionEnum condition) {
        conditionEnums.add(condition);
    }

    public void removeCondition(ConditionEnum condition) {
        conditionEnums.remove(condition);
    }

    public boolean containsCondition(ConditionEnum condition) {
        return conditionEnums.contains(condition);
    }
}

