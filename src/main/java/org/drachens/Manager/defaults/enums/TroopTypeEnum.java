package org.drachens.Manager.defaults.enums;

import net.minestom.server.item.Material;
import org.drachens.animation.Animation;
import org.drachens.animation.DynamicAnimation;
import org.drachens.dataClasses.Armys.TroopType;

public enum TroopTypeEnum {
    ww2(new TroopType(
            7, Material.ORANGE_DYE,
            14, Material.ORANGE_DYE,
            21, Material.ORANGE_DYE,
            new Animation(250L, Material.ORANGE_DYE, new int[]{5, 4, 6, 4}),
            new DynamicAnimation(Material.ORANGE_DYE, new int[][]{{3000, 1}, {200, 2}, {300, 3}}),
            new Animation(1000L, null, null)));

    private final TroopType troopTye;

    TroopTypeEnum(TroopType troopType) {
        this.troopTye = troopType;
    }

    public TroopType getTroopTye() {
        return troopTye;
    }
}
