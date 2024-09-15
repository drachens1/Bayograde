package org.drachens.dataClasses.Diplomacy.Justifications;

public class WarGoalType {
    private final float stabilityEffect;
    private final float duration;
    private final float expires;
    private final String name;

    public WarGoalType(String name, float duration, float stabilityEffect, float expires) {
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

    public String getName() {
        return name;
    }

    public float getExpires() {
        return expires;
    }
}
