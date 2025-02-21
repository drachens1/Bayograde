package org.drachens.interfaces;

import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;

public interface AStarPathfinderVoids {
    boolean isWalkable(Province check, Country country);
}
