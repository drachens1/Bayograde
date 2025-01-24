package org.drachens.dataClasses.Diplomacy;

import org.drachens.dataClasses.Countries.Country;

public class NonAggressionPact {
    private final Country from;
    private final Country to;
    private final float max;
    private float duration;

    public NonAggressionPact(Country from, Country to, float duration) {
        this.from = from;
        this.to = to;
        this.max = duration;
        this.duration = duration;
    }

    public Country getFrom() {
        return from;
    }

    public Country getTo() {
        return to;
    }

    public float getMaxDuration() {
        return max;
    }

    public float getDuration() {
        return duration;
    }

    public void minus(float amount) {
        duration -= amount;
    }
}
