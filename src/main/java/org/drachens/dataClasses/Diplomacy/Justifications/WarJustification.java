package org.drachens.dataClasses.Diplomacy.Justifications;

import com.google.gson.JsonElement;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.interfaces.Saveable;

import java.util.function.Consumer;

public class WarJustification implements Saveable {
    private final Country againstCountry;
    private final Modifier modifier;
    private float timeLeft;
    private float expires;
    private final Consumer<WarJustification> runner;

    public WarJustification(WarGoalType warGoalType, Country againstCountry) {
        this.timeLeft = warGoalType.timeToMake();
        this.againstCountry = againstCountry;
        this.modifier = warGoalType.modifier();
        this.expires = warGoalType.expires();
        this.runner = null;
    }

    public WarJustification(WarGoalType warGoalType, Country againstCountry, Consumer<WarJustification> runner) {
        this.timeLeft = warGoalType.timeToMake();
        this.againstCountry = againstCountry;
        this.modifier = warGoalType.modifier();
        this.expires = warGoalType.expires();
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

    @Override
    public JsonElement toJson() {
        return null;
    }
}
