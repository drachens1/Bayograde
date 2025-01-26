package org.drachens.dataClasses.Diplomacy.Justifications;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.Modifier;

import java.util.function.Consumer;

public class WarJustification {
    private final Country againstCountry;
    private final Modifier modifier;
    private float timeLeft;
    private float expires;
    private final Consumer<WarJustification> runner;

    public WarJustification(WarGoalType warGoalType, Country againstCountry) {
        this.timeLeft = warGoalType.getTimeToMake();
        this.againstCountry = againstCountry;
        this.modifier = warGoalType.getModifier();
        this.expires = warGoalType.getExpires();
        this.runner = null;
    }

    public WarJustification(WarGoalType warGoalType, Country againstCountry, Consumer<WarJustification> runner) {
        this.timeLeft = warGoalType.getTimeToMake();
        this.againstCountry = againstCountry;
        this.modifier = warGoalType.getModifier();
        this.expires = warGoalType.getExpires();
        this.runner = runner;
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

    public void onFinished(){
        if (runner!=null)
            runner.accept(this);
    }
}
