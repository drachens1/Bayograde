package org.drachens.Manager.defaults.enums;

import org.drachens.interfaces.AStarPathfinderVoids;
import org.drachens.temporary.troops.TroopPathing;

public enum PathingEnum {
    ww2(new TroopPathing());

    private final AStarPathfinderVoids aStarPathfinderVoids;
    PathingEnum(AStarPathfinderVoids aStarPathfinderVoids){
        this.aStarPathfinderVoids=aStarPathfinderVoids;
    }
    public AStarPathfinderVoids getaStarPathfinderVoids(){
        return aStarPathfinderVoids;
    }
}
