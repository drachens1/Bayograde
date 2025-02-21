package org.drachens.dataClasses.additional;

import com.google.gson.JsonElement;
import org.drachens.dataClasses.Countries.IdeologyTypes;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.interfaces.Saveable;

public class IdeologyGain implements EventsRunner, Saveable {
    private final Country country;
    private final IdeologyTypes ideologyTypes;
    private final float dailyIncrease;
    private int count;

    public IdeologyGain(Country country, IdeologyTypes ideologyTypes, float dailyIncrease, int length) {
        this.country = country;
        this.ideologyTypes = ideologyTypes;
        this.dailyIncrease = dailyIncrease;
        this.count = length;
    }

    @Override
    public boolean newDay() {
        if (1 > this.count) {
            return true;
        }
        country.getIdeology().addIdeology(ideologyTypes, dailyIncrease);
        count--;
        return false;
    }

    @Override
    public JsonElement toJson() {
        return null;
    }
}
