package org.drachens.dataClasses.Countries;

import it.unimi.dsi.fastutil.Pair;
import org.drachens.dataClasses.VotingOption;

import java.util.*;

public class Ideology {
    private final Country country;
    public float total = 0f;
    private IdeologyTypes currentIdeology;
    private HashMap<IdeologyTypes, Float> ideologies;

    private Ideology(HashMap<IdeologyTypes, Float> ideologies, Country country) {
        this.country = country;
        this.ideologies = new HashMap<>(ideologies);
        normalizeIdeologies();
    }

    public Ideology(VotingOption votingOption, Country country) {
        this.country = country;
        this.ideologies = new HashMap<>();
        for (IdeologyTypes ideologyTypes : votingOption.getIdeologyTypes()) {
            ideologies.put(ideologyTypes, 0f);
        }
        normalizeIdeologies();
    }

    public Ideology(VotingOption votingOption) {
        this.country = null;
        this.ideologies = new HashMap<>();
        for (IdeologyTypes ideologyTypes : votingOption.getIdeologyTypes()) {
            ideologies.put(ideologyTypes, 0f);
        }
        normalizeIdeologies();
    }

    private void normalizeIdeologies() {
        float totalPercentage = 0f;

        for (Float value : ideologies.values()) {
            totalPercentage += value;
        }

        if (totalPercentage < 100f) {
            float deficit = 100f - totalPercentage;
            int ideologyCount = ideologies.size();
            float increment = deficit / ideologyCount;

            for (Map.Entry<IdeologyTypes, Float> entry : ideologies.entrySet()) {
                ideologies.put(entry.getKey(), entry.getValue() + increment);
            }
        }
        for (Map.Entry<IdeologyTypes, Float> entry : ideologies.entrySet()) {
            total += entry.getValue();
        }
    }

    public IdeologyTypes getCurrentIdeology() {
        return currentIdeology;
    }

    public void setCurrentIdeology(IdeologyTypes currentIdeology) {
        this.currentIdeology = currentIdeology;
    }

    public HashMap<IdeologyTypes, Float> getIdeologies() {
        return ideologies;
    }

    public void setIdeologies(HashMap<IdeologyTypes, Float> ideologies) {
        this.ideologies = ideologies;
        normalizeIdeologies();
    }

    public void addIdeology(IdeologyTypes ideology, float percentage) {
        if (ideologies.containsKey(ideology)) percentage += ideologies.get(ideology);
        if (percentage < 0f) {
            percentage = 0;
        } else if (percentage > 100f) percentage = 100f;
        float timesAmount = percentage / 100f;
        for (Map.Entry<IdeologyTypes, Float> entry : ideologies.entrySet()) {
            float currentPercentage = entry.getValue();
            ideologies.put(entry.getKey(), currentPercentage * timesAmount);
        }
        ideologies.put(ideology, percentage);

        changeLeadingIdeology();
    }

    public void changeLeadingIdeology() {
        Pair<IdeologyTypes, Float> highest = new Pair<>() {
            @Override
            public IdeologyTypes left() {
                return currentIdeology;
            }

            @Override
            public Float right() {
                return ideologies.get(currentIdeology);
            }
        };
        for (Map.Entry<IdeologyTypes, Float> e : ideologies.entrySet()) {
            if (highest.right() == null || highest.right() < e.getValue()) {
                highest = new Pair<>() {
                    @Override
                    public IdeologyTypes left() {
                        return e.getKey();
                    }

                    @Override
                    public Float right() {
                        return e.getValue();
                    }
                };
            }
        }
        currentIdeology = highest.left();
        List<Leader> leaders = new ArrayList<>(currentIdeology.getLeaders());
        if (!leaders.contains(country.getLeader())) {
            if (country.getLeader() != null)
                country.removeModifier(country.getLeader().getIdeologyTypes().getModifier());
            country.setLeader(leaders.get(new Random().nextInt(0, leaders.size())));
        }
    }

    public Ideology clone(Country country) {
        return new Ideology(ideologies, country);
    }
}
