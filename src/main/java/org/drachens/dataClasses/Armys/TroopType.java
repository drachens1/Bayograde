package org.drachens.dataClasses.Armys;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.animation.AnimationType;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class TroopType {
    private final AnimationType moveAnimation;
    private final AnimationType shootingAnimation;
    private final AnimationType standstillAnimation;
    private final ItemStack ownTroop;
    private final ItemStack allyTroop;
    private final ItemStack enemyTroop;

    public TroopType(int friendlyModelData, Material friendlyItem, int allyModelData, Material allyItem, int enemyModelData, Material enemyItem, AnimationType moveAnimation, AnimationType shootingAnimation, AnimationType standstillAnimation) {
        ownTroop = itemBuilder(friendlyItem, friendlyModelData);
        allyTroop = itemBuilder(allyItem, allyModelData);
        enemyTroop = itemBuilder(enemyItem, enemyModelData);
        this.moveAnimation = moveAnimation;
        this.shootingAnimation = shootingAnimation;
        this.standstillAnimation = standstillAnimation;
    }

    public AnimationType getMoveAnimation() {
        return moveAnimation;
    }

    public AnimationType getShootingAnimation() {
        return shootingAnimation;
    }

    public AnimationType getStandstillAnimation() {
        return standstillAnimation;
    }

    public ItemStack getOwnTroop() {
        return ownTroop;
    }

    public ItemStack getAllyTroop() {
        return allyTroop;
    }

    public ItemStack getEnemyTroop() {
        return enemyTroop;
    }
}
