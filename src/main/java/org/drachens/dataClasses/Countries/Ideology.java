package org.drachens.dataClasses.Countries;

import it.unimi.dsi.fastutil.Pair;

import java.util.HashMap;
import java.util.Map;

public class Ideology implements Cloneable {
    private IdeologyTypes currentIdeology;
    private HashMap<IdeologyTypes, Float> ideologies;
    public Ideology(HashMap<IdeologyTypes, Float> ideologies){
        this.ideologies = ideologies;
    }
    public IdeologyTypes getCurrentIdeology() {
        return currentIdeology;
    }

    public void setCurrentIdeology(IdeologyTypes currentIdeology) {
        this.currentIdeology = currentIdeology;
    }

    public void setIdeologies(HashMap<IdeologyTypes, Float> ideologies) {
        this.ideologies = ideologies;
    }

    public HashMap<IdeologyTypes, Float> getIdeologies() {
        return ideologies;
    }

    public void addIdeology(IdeologyTypes ideology, float percentage){
        System.out.println(percentage);
        if (ideologies.containsKey(ideology))percentage+=ideologies.get(ideology);
        if (percentage < 0f){
            percentage = 0;
        }else if (percentage>100f)percentage=100f;

        float timesAmount = percentage/100f;
        System.out.println("Times amount: "+timesAmount);
        for (Map.Entry<IdeologyTypes, Float> entry : ideologies.entrySet()) {
            float currentPercentage = entry.getValue();
            ideologies.put(entry.getKey(), currentPercentage * timesAmount);
        }
        ideologies.put(ideology,percentage);

        changeLeadingIdeology();
        System.out.println(ideologies);
    }

    public void changeLeadingIdeology(){
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
        for (Map.Entry<IdeologyTypes, Float> e : ideologies.entrySet()){
            if (highest.right()==null || highest.right() < e.getValue()){
                highest=new Pair<>() {
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
    }
    @Override
    protected Object clone()  {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
