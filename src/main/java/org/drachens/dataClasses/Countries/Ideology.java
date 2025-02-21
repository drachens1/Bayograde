package org.drachens.dataClasses.Countries;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.Pair;
import lombok.Getter;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.VotingOption;
import org.drachens.interfaces.Saveable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static org.drachens.util.JsonUtil.saveHashMap;

@Getter
public class Ideology implements Saveable {
    private final Country country;
    private final HashMap<IdeologyTypes, Float> ideologies;
    public float total = 0f;
    private IdeologyTypes currentIdeology;

    private Ideology(HashMap<IdeologyTypes, Float> ideologies, Country country) {
        this.country = country;
        this.ideologies = new HashMap<>(ideologies);
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

            ideologies.replaceAll((k, v) -> v + increment);
        }
        for (Map.Entry<IdeologyTypes, Float> entry : ideologies.entrySet()) {
            total += entry.getValue();
        }
    }

    public void setCurrentIdeology(IdeologyTypes currentIdeology) {
        if (this.currentIdeology != null) {
            country.removeModifier(this.currentIdeology.getModifier());
        }
        country.addModifier(currentIdeology.getModifier());
        this.currentIdeology = currentIdeology;
    }

    public void addIdeology(IdeologyTypes ideology, float percentage) {
        if (ideologies.containsKey(ideology)) {
            percentage += ideologies.get(ideology);
        }

        if (percentage < 0f) {
            percentage = 0f;
        } else if (percentage > 100f) {
            percentage = 100f;
        }

        float totalPercentage = total + percentage - (ideologies.getOrDefault(ideology, 0f));

        if (totalPercentage > 100f) {
            float scaleFactor = 100f / totalPercentage;
            for (Map.Entry<IdeologyTypes, Float> entry : ideologies.entrySet()) {
                float currentPercentage = entry.getValue();
                ideologies.put(entry.getKey(), currentPercentage * scaleFactor);
            }
        }
        ideologies.put(ideology, percentage);
        total = 0f;
        for (Float value : ideologies.values()) {
            total += value;
        }
    }

    public void changeLeadingIdeology() {
        Pair<IdeologyTypes, Float> highest = getIdeologyTypesFloatPair();

        if (currentIdeology == null) {
            if (country.getInfo().getLeader() == null) {
                List<Leader> leaders = highest.left().getLeaders();
                country.setLeader(leaders.get(new Random().nextInt(0, leaders.size())));
                currentIdeology = highest.left();
                return;
            } else {
                currentIdeology = country.getInfo().getLeader().getIdeologyTypes();
            }
        }
        List<Leader> leaders = new ArrayList<>(currentIdeology.getLeaders());
        if (currentIdeology != highest.left()) {
            if (country.getInfo().getLeader() != null)
                country.removeModifier(country.getInfo().getLeader().getIdeologyTypes().getModifier());
            country.setLeader(leaders.get(new Random().nextInt(0, leaders.size())));
        }
        currentIdeology = highest.left();
    }

    private @NotNull Pair<IdeologyTypes, Float> getIdeologyTypesFloatPair() {
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
        return highest;
    }

    public Ideology clone(Country country) {
        return new Ideology(ideologies, country);
    }

    public List<String> getIdeologyNames() {
        List<String> s = new ArrayList<>();
        ideologies.keySet().forEach(ideologyTypes -> s.add(ideologyTypes.getIdentifier()));
        return s;
    }

    @Override
    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("Current",currentIdeology.toJson());
        jsonObject.add("total",new JsonPrimitive(total));
        HashMap<String,Float> ideologiesCopy = new HashMap<>();
        ideologies.forEach((ideologyTypes, aFloat) -> ideologiesCopy.put(ideologyTypes.getIdentifier(),aFloat));
        saveHashMap(ideologiesCopy,Float.class,jsonObject,"ideology");
        return jsonObject;
    }
}
