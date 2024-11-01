package org.drachens.temporary;

import org.drachens.dataClasses.AStarPathfinderVoids;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Provinces.Province;

public class TroopPathing implements AStarPathfinderVoids {
    @Override
    public boolean isWalkable(Province check, Country country) {
        return check != null && check.isCapturable() && (check.getOccupier()==country || check.getOccupier().isMilitaryAlly(country));
    }

    @Override
    public double calcPrio(Province p) {
        return 0;
    }
}
