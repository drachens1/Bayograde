package org.drachens.dataClasses.Armys;

import net.minestom.server.item.Material;
import org.drachens.animation.Animation;
import org.drachens.animation.AnimationType;
import org.drachens.animation.DynamicAnimation;

public class TroopType {
    private final AnimationType moveAnimation;
    private final AnimationType shootingAnimation;
    private final AnimationType standstillAnimation;
    private final Material item;
    private final int modelData;
    public TroopType(int modelData, Material item, AnimationType moveAnimation, AnimationType shootingAnimation, AnimationType standstillAnimation){
        this.item = item;
        this.modelData = modelData;
        this.moveAnimation = moveAnimation;
        this.shootingAnimation = shootingAnimation;
        this.standstillAnimation = standstillAnimation;
    }
    public AnimationType getMoveAnimation(){
        return moveAnimation;
    }
    public AnimationType getShootingAnimation(){
        return shootingAnimation;
    }
    public AnimationType getStandstillAnimation(){
        return standstillAnimation;
    }
    public Material getItem(){
        return item;
    }
    public int getModelData(){
        return modelData;
    }
}
