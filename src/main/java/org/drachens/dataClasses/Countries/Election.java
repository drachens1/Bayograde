package org.drachens.dataClasses.Countries;

import org.drachens.Manager.defaults.enums.ElectionsEnum;
import org.drachens.dataClasses.VotingOption;

import java.util.HashMap;
import java.util.Map;

public class Election {
    private ElectionsEnum currentElectionType;
    private HashMap<ElectionsEnum, Float> electionTypesHashMap;

    public Election(VotingOption votingOption) {
        this.electionTypesHashMap = new HashMap<>();
        for (ElectionsEnum electionTypes : votingOption.getElectionTypes()) {
            electionTypesHashMap.put(electionTypes, 0f);
        }
    }

    public ElectionsEnum getCurrentElectionType() {
        return currentElectionType;
    }

    public void setCurrentElection(ElectionsEnum currentElectionType) {
        this.currentElectionType = currentElectionType;
    }

    public HashMap<ElectionsEnum, Float> getElections() {
        return electionTypesHashMap;
    }

    public void setElections(HashMap<ElectionsEnum, Float> electionTypes) {
        this.electionTypesHashMap = electionTypes;
    }

    public void addElection(ElectionsEnum electionType, float percentage) {
        if (electionType == null || percentage < 0f) {
            throw new IllegalArgumentException("Election type cannot be null and percentage must be non-negative.");
        }

        percentage = Math.min(percentage, 100f);

        float totalPercentage = 0f;
        for (Float value : electionTypesHashMap.values()) {
            totalPercentage += value;
        }

        if (electionTypesHashMap.containsKey(electionType)) {
            totalPercentage -= electionTypesHashMap.get(electionType);
        }

        float remainingPercentage = 100f - percentage;
        if (remainingPercentage < totalPercentage) {
            float scaleFactor = remainingPercentage / totalPercentage;
            for (Map.Entry<ElectionsEnum, Float> entry : electionTypesHashMap.entrySet()) {
                if (!entry.getKey().equals(electionType)) {
                    entry.setValue(entry.getValue() * scaleFactor);
                }
            }
        }

        electionTypesHashMap.put(electionType, percentage);

        changeLeadingElection();
    }

    public void changeLeadingElection() {
        ElectionsEnum leadingElection = null;
        float highestPercentage = -1f;

        for (Map.Entry<ElectionsEnum, Float> entry : electionTypesHashMap.entrySet()) {
            if (entry.getValue() > highestPercentage) {
                leadingElection = entry.getKey();
                highestPercentage = entry.getValue();
            }
        }

        currentElectionType = leadingElection;
    }
}

