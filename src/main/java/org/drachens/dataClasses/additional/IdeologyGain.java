package org.drachens.dataClasses.additional;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.IdeologyTypes;

public class IdeologyGain implements EventsRunner {
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
        if (count < 1) {
            return true;
        }
        country.getIdeology().addIdeology(ideologyTypes, dailyIncrease);
        count--;
        return false;
    }
}
