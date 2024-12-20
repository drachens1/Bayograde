package org.drachens.dataClasses.Countries;

import it.unimi.dsi.fastutil.Pair;
import org.drachens.Manager.defaults.enums.ElectionsEnum;
import org.drachens.dataClasses.VotingOption;

import java.util.HashMap;
import java.util.Map;

public class Election {
    private final Country country;
    private ElectionsEnum currentElectionType;
    private HashMap<ElectionsEnum, Float> electionTypesHashMap;

    private Election(HashMap<ElectionsEnum, Float> electionTypes, Country country) {
        this.electionTypesHashMap = new HashMap<>(electionTypes);
        this.country = country;
    }

    public Election(VotingOption votingOption) {
        this.electionTypesHashMap = new HashMap<>();
        for (ElectionsEnum electionTypes : votingOption.getElectionTypes()) {
            electionTypesHashMap.put(electionTypes, 0f);
        }
        this.country = null;
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

    public void addElection(ElectionsEnum electionTypes, float percentage) {
        if (electionTypesHashMap.containsKey(electionTypes)) percentage += electionTypesHashMap.get(electionTypes);
        if (percentage < 0f) {
            percentage = 0;
        } else if (percentage > 100f) percentage = 100f;
        float timesAmount = percentage / 100f;
        for (Map.Entry<ElectionsEnum, Float> entry : electionTypesHashMap.entrySet()) {
            float currentPercentage = entry.getValue();
            electionTypesHashMap.put(entry.getKey(), currentPercentage * timesAmount);
        }
        electionTypesHashMap.put(electionTypes, percentage);

        changeLeadingElection();
    }

    public void changeLeadingElection() {
        Pair<ElectionsEnum, Float> highest = new Pair<>() {
            @Override
            public ElectionsEnum left() {
                return currentElectionType;
            }

            @Override
            public Float right() {
                return electionTypesHashMap.get(currentElectionType);
            }
        };
        for (Map.Entry<ElectionsEnum, Float> e : electionTypesHashMap.entrySet()) {
            if (highest.right() == null || highest.right() < e.getValue()) {
                highest = new Pair<>() {
                    @Override
                    public ElectionsEnum left() {
                        return e.getKey();
                    }

                    @Override
                    public Float right() {
                        return e.getValue();
                    }
                };
            }
        }
        currentElectionType = highest.left();
    }

    public Election clone(Country country) {
        return new Election(electionTypesHashMap, country);
    }
}
