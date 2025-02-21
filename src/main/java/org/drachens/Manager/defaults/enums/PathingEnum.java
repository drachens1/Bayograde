package org.drachens.Manager.defaults.enums;

import lombok.Getter;
import org.drachens.generalGame.troops.TroopPathing;
import org.drachens.interfaces.AStarPathfinderVoids;

@Getter
public enum PathingEnum {
    ww2(new TroopPathing());

    private final AStarPathfinderVoids aStarPathfinderVoids;
    PathingEnum(AStarPathfinderVoids aStarPathfinderVoids){
        this.aStarPathfinderVoids=aStarPathfinderVoids;
    }
}
