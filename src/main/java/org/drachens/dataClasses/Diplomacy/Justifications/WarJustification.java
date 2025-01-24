package org.drachens.dataClasses.Diplomacy.Justifications;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.Modifier;

public class WarJustification {
    private final Country againstCountry;
    private final Modifier modifier;
    private float timeLeft;
    private float expires;

    public WarJustification(WarGoalType warGoalType, Country againstCountry) {
        this.timeLeft = warGoalType.getTimeToMake();
        this.againstCountry = againstCountry;
        this.modifier = warGoalType.getModifier();
        this.expires = warGoalType.getExpires();
    }

    public float getTimeLeft() {
        return timeLeft;
    }

    public void minusTimeLeft(float amount) {
        timeLeft -= amount;
    }

    public Country getAgainstCountry() {
        return againstCountry;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public float getExpires() {
        return expires;
    }

    public void minusExpires(float amount) {
        expires -= amount;
    }
}
