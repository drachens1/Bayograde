package org.drachens.generalGame.troops;

import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.AStarPathfinderVoids;

public class TroopPathing implements AStarPathfinderVoids {
    @Override
    public boolean isWalkable(Province check, Country country) {
        if (null == check || null == check.getOccupier()) return false;
        int c = country.getDiplomacy().getDiplomaticRelation(check.getOccupier().getName());
        return 1 == c || 3 < c || check.getOccupier() == country;
    }
}
