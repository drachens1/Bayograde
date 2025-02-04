package org.drachens.Manager.defaults.enums;

import net.minestom.server.item.Material;
import org.drachens.animation.Animation;
import org.drachens.animation.DynamicAnimation;
import org.drachens.dataClasses.Armys.TroopType;

public enum TroopTypeEnum {
    ww2(new TroopType(
            5, Material.ORANGE_DYE,
            15, Material.ORANGE_DYE,
            10, Material.ORANGE_DYE,
            new Animation(250L, Material.ORANGE_DYE, new int[]{5, 3, 4}),
            new DynamicAnimation(Material.ORANGE_DYE, new int[][]{{3000, 5}, {200, 2}, {300, 3}}),
            new Animation(1000L, null, null)));

    private final TroopType troopTye;

    TroopTypeEnum(TroopType troopType) {
        this.troopTye = troopType;
    }

    public TroopType getTroopTye() {
        return troopTye;
    }
}
