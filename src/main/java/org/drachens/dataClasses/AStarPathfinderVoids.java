package org.drachens.dataClasses;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;

public interface AStarPathfinderVoids {
    boolean isWalkable(Province check, Country country);

    double calcPrio(Province p);
}
