package org.drachens.Manager.defaults.enums;

import lombok.Getter;
import net.minestom.server.item.Material;
import org.drachens.animation.Animation;
import org.drachens.animation.DynamicAnimation;
import org.drachens.dataClasses.Armys.TroopType;

@Getter
public enum TroopTypeEnum {
    ww2(new TroopType(
            6, Material.ORANGE_DYE,
            18, Material.ORANGE_DYE,
            12, Material.ORANGE_DYE,
            new Animation(250L, Material.ORANGE_DYE, new int[]{6, 5, 4}),
            new DynamicAnimation(Material.ORANGE_DYE, new int[][]{{3000, 1}, {200, 2}, {300, 3}}),
            new Animation(1000L, null, null)));

    private final TroopType troopTye;

    TroopTypeEnum(TroopType troopType) {
        this.troopTye = troopType;
    }

}
