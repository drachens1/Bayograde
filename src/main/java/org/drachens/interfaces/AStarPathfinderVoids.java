package org.drachens.interfaces;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;

public interface AStarPathfinderVoids {
    boolean isWalkable(Province check, Country country);
}
