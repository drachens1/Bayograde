package org.drachens.dataClasses.Countries;

import it.unimi.dsi.fastutil.Pair;
import org.drachens.interfaces.VotingOption;

import java.util.HashMap;
import java.util.Map;

public class Election {
    private final Country country;
    private ElectionTypes currentElectionType;
    private HashMap<ElectionTypes, Float> electionTypesHashMap;

    private Election(HashMap<ElectionTypes, Float> electionTypes, Country country) {
        this.electionTypesHashMap = new HashMap<>(electionTypes);
        this.country = country;
    }

    public Election(VotingOption votingOption) {
        this.electionTypesHashMap = new HashMap<>();
        for (ElectionTypes electionTypes : votingOption.getElectionTypes()) {
            electionTypesHashMap.put(electionTypes, 0f);
        }
        this.country = null;
    }

    public ElectionTypes getCurrentElectionType() {
        return currentElectionType;
    }

    public void setCurrentElection(ElectionTypes currentElectionType) {
        this.currentElectionType = currentElectionType;
    }

    public void setElections(HashMap<ElectionTypes, Float> electionTypes) {
        this.electionTypesHashMap = electionTypes;
    }

    public HashMap<ElectionTypes, Float> getElections() {
        return electionTypesHashMap;
    }

    public void addElection(ElectionTypes electionTypes, float percentage) {
        if (electionTypesHashMap.containsKey(electionTypes)) percentage += electionTypesHashMap.get(electionTypes);
        if (percentage < 0f) {
            percentage = 0;
        } else if (percentage > 100f) percentage = 100f;
        float timesAmount = percentage / 100f;
        for (Map.Entry<ElectionTypes, Float> entry : electionTypesHashMap.entrySet()) {
            float currentPercentage = entry.getValue();
            electionTypesHashMap.put(entry.getKey(), currentPercentage * timesAmount);
        }
        electionTypesHashMap.put(electionTypes, percentage);

        changeLeadingElection();
    }

    public void changeLeadingElection() {
        Pair<ElectionTypes, Float> highest = new Pair<>() {
            @Override
            public ElectionTypes left() {
                return currentElectionType;
            }

            @Override
            public Float right() {
                return electionTypesHashMap.get(currentElectionType);
            }
        };
        for (Map.Entry<ElectionTypes, Float> e : electionTypesHashMap.entrySet()) {
            if (highest.right() == null || highest.right() < e.getValue()) {
                highest = new Pair<>() {
                    @Override
                    public ElectionTypes left() {
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
