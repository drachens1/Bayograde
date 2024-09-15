package org.drachens.dataClasses.Diplomacy.Justifications;

import org.drachens.dataClasses.Countries.Country;

public class WarGoal {
    private final float expires;
    private final Country againstCountry;
    private final float stabilityEffect;

    public WarGoal(float expires, Country againstCountry, float stabilityEffect) {
        this.expires = expires;
        this.againstCountry = againstCountry;
        this.stabilityEffect = stabilityEffect;
    }

    public WarGoal(WarJustification warJustification) {
        this.againstCountry = warJustification.getAgainstCountry();
        this.stabilityEffect = warJustification.getStabilityEffect();
        this.expires = warJustification.getExpires();
    }

    public float getExpires() {
        return expires;
    }

    public Country getAgainstCountry() {
        return againstCountry;
    }

    public float getStabilityEffect() {
        return stabilityEffect;
    }
}
