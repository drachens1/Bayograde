package org.drachens.dataClasses.Diplomacy.Justifications;

import org.drachens.dataClasses.Countries.Country;

public class WarJustification {
    private final float timeLeft;
    private final Country againstCountry;
    private final float stabilityEffect;
    private final float expires;
    public WarJustification(float timeLeft, Country againstCountry, float stabilityEffect, float expires){
        this.timeLeft=timeLeft;
        this.againstCountry=againstCountry;
        this.stabilityEffect=stabilityEffect;
        this.expires = expires;
    }
    public WarJustification(WarGoalType warGoalType, Country againstCountry){
        this.timeLeft = warGoalType.getDuration();
        this.againstCountry = againstCountry;
        this.stabilityEffect = warGoalType.getStabilityEffect();
        this.expires = warGoalType.getExpires();
    }

    public float getTimeLeft() {
        return timeLeft;
    }

    public Country getAgainstCountry() {
        return againstCountry;
    }

    public float getStabilityEffect() {
        return stabilityEffect;
    }
    public float getExpires(){
        return expires;
    }
}
