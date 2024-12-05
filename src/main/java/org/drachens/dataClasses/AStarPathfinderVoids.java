package org.drachens.dataClasses;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.territories.Province;

public interface AStarPathfinderVoids {
    boolean isWalkable(Province check, Country country);

    double calcPrio(Province p);
}
