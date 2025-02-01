package org.drachens.temporary.troops;

import org.drachens.dataClasses.AStarPathfinderVoids;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;

public class TroopPathing implements AStarPathfinderVoids {
    @Override
    public boolean isWalkable(Province check, Country country) {
        if (check==null||check.getOccupier()==null)return false;
        int c = country.getDiplomacy(check.getOccupier().getName());
        return  c==1 || c>3 || check.getOccupier()==country;
    }

    @Override
    public double calcPrio(Province p) {
        return 0;
    }
}
