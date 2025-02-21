package org.drachens.dataClasses.Armys;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.drachens.animation.Animation;
import org.drachens.animation.AnimationType;
import org.drachens.animation.DynamicAnimation;

import java.util.Arrays;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class TroopType {
    private final Animation moveAnimation;
    private final DynamicAnimation shootingAnimation;
    private final AnimationType standstillAnimation;
    private final ItemStack ownTroop;
    private final ItemStack allyTroop;
    private final Animation allyMoveAnimation;
    private final DynamicAnimation allyShootAnimation;
    private final ItemStack enemyTroop;
    private final Animation enemyMoveAnimation;
    private final DynamicAnimation enemyShootAnimation;

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
            System.out.println("EnemyShoot: "+enemyShootingFrames[i][1]);
        }
        this.enemyShootAnimation =new DynamicAnimation(shootingAnimation.getItem(),enemyShootingFrames);

        int[] enemyMoveFrames = moveAnimation.getFrames().clone();
        for (int i = 0; i < enemyMoveFrames.length; i++) {
            enemyMoveFrames[i] += 6;
            System.out.println("EnemyMove: "+enemyMoveFrames[i]);
        }
        this.enemyMoveAnimation =new Animation(moveAnimation.getDelayBetween(), moveAnimation.getItem(), enemyMoveFrames);

        int[][] allyShootingFrames = Arrays.stream(shootingAnimation.getFrames())
                .map(int[]::clone)
                .toArray(int[][]::new);
        for (int i = 0; i < allyShootingFrames.length; i++) {
            allyShootingFrames[i][1] += 12;
            System.out.println("AllyShoot: "+allyShootingFrames[i][1]);
        }
        this.allyShootAnimation =new DynamicAnimation(shootingAnimation.getItem(),allyShootingFrames);

        int[] allyMoveFrames = moveAnimation.getFrames().clone();
        for (int i = 0; i < allyMoveFrames.length; i++) {
            allyMoveFrames[i] += 12;
            System.out.println("AllyMove: "+allyMoveFrames[i]);
        }
        this.allyMoveAnimation =new Animation(moveAnimation.getDelayBetween(), moveAnimation.getItem(), allyMoveFrames);
    }

    public Animation getMoveAnimation() {
        return moveAnimation;
    }

    public DynamicAnimation getShootingAnimation() {
        return shootingAnimation;
    }

    public AnimationType getStandstillAnimation() {
        return standstillAnimation;
    }

    public Animation getEnemyMoveAnimation() {
        return enemyMoveAnimation;
    }

    public DynamicAnimation getEnemyShootingAnimation() {
        return enemyShootAnimation;
    }

    public Animation getAllyMoveAnimation() {
        return allyMoveAnimation;
    }

    public DynamicAnimation getAllyShootingAnimation() {
        return allyShootAnimation;
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
