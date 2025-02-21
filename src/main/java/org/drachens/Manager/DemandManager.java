package org.drachens.Manager;

import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Demand;

import java.util.HashMap;

public class DemandManager {
    private final HashMap<Country, Demand> active = new HashMap<>();

    public boolean isPlayerActive(Country country) {
        return active.containsKey(country);
    }

    public void addActive(Country country, Demand demand) {
        active.put(country, demand);
    }

    public void removeActive(Country country) {
        active.remove(country);
    }

    public Demand getDemand(Country country) {
        return active.get(country);
    }
}
