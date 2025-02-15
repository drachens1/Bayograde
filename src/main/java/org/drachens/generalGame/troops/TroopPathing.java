package org.drachens.generalGame.troops;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Province;
import org.drachens.interfaces.AStarPathfinderVoids;

public class TroopPathing implements AStarPathfinderVoids {
    @Override
    public boolean isWalkable(Province check, Country country) {
        if (check==null||check.getOccupier()==null)return false;
        int c = country.getDiplomacy(check.getOccupier().getName());
        return  c==1 || c>3 || check.getOccupier()==country;
    }
}
