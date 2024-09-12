package org.drachens.dataClasses;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;

public interface AStarPathfinderVoids {
    public boolean isWalkable(Province check, Country country);
    public double calcPrio(Province p);
}
