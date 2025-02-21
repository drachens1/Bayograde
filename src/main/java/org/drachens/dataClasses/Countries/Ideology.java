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
    public float total;
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
            ideologies.put(ideologyTypes, 0.0f);
        }
        normalizeIdeologies();
    }

    private void normalizeIdeologies() {
        float totalPercentage = 0.0f;

        for (Float value : ideologies.values()) {
            totalPercentage += value;
        }

        if (100.0f > totalPercentage) {
            float deficit = 100.0f - totalPercentage;
            int ideologyCount = ideologies.size();
            float increment = deficit / ideologyCount;

            ideologies.replaceAll((k, v) -> v + increment);
        }
        for (Map.Entry<IdeologyTypes, Float> entry : ideologies.entrySet()) {
            total += entry.getValue();
        }
    }

    public void setCurrentIdeology(IdeologyTypes currentIdeology) {
        if (null != this.currentIdeology) {
            country.removeModifier(this.currentIdeology.getModifier());
        }
        country.addModifier(currentIdeology.getModifier());
        this.currentIdeology = currentIdeology;
    }

    public void addIdeology(IdeologyTypes ideology, float percentage) {
        if (ideologies.containsKey(ideology)) {
            percentage += ideologies.get(ideology);
        }

        if (0.0f > percentage) {
            percentage = 0.0f;
        } else if (100.0f < percentage) {
            percentage = 100.0f;
        }

        float totalPercentage = this.total + percentage - ideologies.getOrDefault(ideology, 0.0f);

        if (100.0f < totalPercentage) {
            float scaleFactor = 100.0f / totalPercentage;
            for (Map.Entry<IdeologyTypes, Float> entry : ideologies.entrySet()) {
                float currentPercentage = entry.getValue();
                ideologies.put(entry.getKey(), currentPercentage * scaleFactor);
            }
        }
        ideologies.put(ideology, percentage);
        total = 0.0f;
        for (Float value : ideologies.values()) {
            total += value;
        }
    }

    public void changeLeadingIdeology() {
        Pair<IdeologyTypes, Float> highest = getIdeologyTypesFloatPair();

        if (null == this.currentIdeology) {
            if (null == this.country.getInfo().getLeader()) {
                List<Leader> leaders = highest.left().getLeaders();
                country.setLeader(leaders.get(new Random().nextInt(0, leaders.size())));
                currentIdeology = highest.left();
                return;
            }
            currentIdeology = country.getInfo().getLeader().getIdeologyTypes();
        }
        List<Leader> leaders = new ArrayList<>(currentIdeology.getLeaders());
        if (currentIdeology != highest.left()) {
            if (null != this.country.getInfo().getLeader())
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
            if (null == highest.right() || highest.right() < e.getValue()) {
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
        jsonObject.add("Current", currentIdeology.toJson());
        jsonObject.add("total",new JsonPrimitive(total));
        HashMap<String,Float> ideologiesCopy = new HashMap<>();
        ideologies.forEach((ideologyTypes, aFloat) -> ideologiesCopy.put(ideologyTypes.getIdentifier(),aFloat));
        saveHashMap(ideologiesCopy,Float.class,jsonObject,"ideology");
        return jsonObject;
    }
}
