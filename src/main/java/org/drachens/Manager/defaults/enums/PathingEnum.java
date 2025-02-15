package org.drachens.Manager.defaults.enums;

import org.drachens.generalGame.troops.TroopPathing;
import org.drachens.interfaces.AStarPathfinderVoids;

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
