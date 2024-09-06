package org.drachens.dataClasses.Diplomacy.Justifications;

public class WarGoalType {
    private float stabilityEffect;
    private float duration;
    private float expires;
    private String name;
    public WarGoalType(String name, float duration, float stabilityEffect, float expires){
        this.stabilityEffect = stabilityEffect;
        this.duration = duration;
        this.name = name;
        this.expires = expires;
    }
    public float getStabilityEffect() {
        return stabilityEffect;
    }
    public float getDuration() {
        return duration;
    }
    public String getName(){
        return name;
    }
    public float getExpires(){
        return expires;
    }
}
