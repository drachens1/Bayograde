package org.drachens.dataClasses;

import org.drachens.dataClasses.Countries.Country;

public interface AStarPathfinderVoids {
    boolean isWalkable(Province check, Country country);

    double calcPrio(Province p);
}
