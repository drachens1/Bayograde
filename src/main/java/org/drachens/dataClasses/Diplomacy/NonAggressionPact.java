package org.drachens.dataClasses.Diplomacy;

import com.google.gson.JsonElement;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Saveable;

public class NonAggressionPact implements Saveable {
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

    @Override
    public JsonElement toJson() {
        return null;
    }
}
