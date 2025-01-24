package org.drachens.dataClasses.Diplomacy.Justifications;

import org.drachens.dataClasses.additional.Modifier;

public class WarGoalType {
    private final Modifier modifier;
    private final float timeToMake;
    private final float expires;
    private final String name;

    public WarGoalType(String name, Modifier modifier, float expires, float timeToMake) {
        this.modifier = modifier;
        this.timeToMake = timeToMake;
        this.name = name;
        this.expires = expires;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public float getTimeToMake() {
        return timeToMake;
    }

    public String getName() {
        return name;
    }

    public float getExpires() {
        return expires;
    }
}
