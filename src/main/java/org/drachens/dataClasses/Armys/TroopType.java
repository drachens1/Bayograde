package org.drachens.dataClasses.Armys;

import lombok.Getter;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.animation.Animation;
import org.drachens.animation.AnimationType;
import org.drachens.animation.DynamicAnimation;

import java.util.Arrays;

import static org.drachens.util.ItemStackUtil.itemBuilder;

@Getter
public class TroopType {
    private final Animation moveAnimation;
    private final DynamicAnimation shootingAnimation;
    private final AnimationType standstillAnimation;
    private final ItemStack ownTroop;
    private final ItemStack allyTroop;
    private final Animation allyMoveAnimation;
    private final DynamicAnimation allyShootingAnimation;
    private final ItemStack enemyTroop;
    private final Animation enemyMoveAnimation;
    private final DynamicAnimation enemyShootingAnimation;

    public TroopType(int friendlyModelData, Material friendlyItem, int allyModelData, Material allyItem, int enemyModelData, Material enemyItem, Animation moveAnimation, DynamicAnimation shootingAnimation, AnimationType standstillAnimation) {
        ownTroop = itemBuilder(friendlyItem, friendlyModelData);
        allyTroop = itemBuilder(allyItem, allyModelData);
        enemyTroop = itemBuilder(enemyItem, enemyModelData);
        this.moveAnimation = moveAnimation;
        this.shootingAnimation = shootingAnimation;
        this.standstillAnimation = standstillAnimation;
        int[][] enemyShootingFrames = Arrays.stream(shootingAnimation.getFrames())
                .map(int[]::clone)
                .toArray(int[][]::new);
        for (int i = 0; i < enemyShootingFrames.length; i++) {
            enemyShootingFrames[i][1] += 6;
        }
        this.enemyShootingAnimation =new DynamicAnimation(shootingAnimation.getItem(),enemyShootingFrames);

        int[] enemyMoveFrames = moveAnimation.getFrames().clone();
        for (int i = 0; i < enemyMoveFrames.length; i++) {
            enemyMoveFrames[i] += 6;
        }
        this.enemyMoveAnimation =new Animation(moveAnimation.getDelayBetween(), moveAnimation.getItem(), enemyMoveFrames);

        int[][] allyShootingFrames = Arrays.stream(shootingAnimation.getFrames())
                .map(int[]::clone)
                .toArray(int[][]::new);
        for (int i = 0; i < allyShootingFrames.length; i++) {
            allyShootingFrames[i][1] += 12;
        }
        this.allyShootingAnimation =new DynamicAnimation(shootingAnimation.getItem(),allyShootingFrames);

        int[] allyMoveFrames = moveAnimation.getFrames().clone();
        for (int i = 0; i < allyMoveFrames.length; i++) {
            allyMoveFrames[i] += 12;
        }
        this.allyMoveAnimation =new Animation(moveAnimation.getDelayBetween(), moveAnimation.getItem(), allyMoveFrames);
    }
}
