package org.drachens.animation;

import org.drachens.dataClasses.other.ItemDisplay;

import java.util.List;

public class CombinedAnimation extends AnimationType {
    private final List<AnimationType> animationTypes;

    public CombinedAnimation(AnimationType... animationTypes) {
        this.animationTypes = List.of(animationTypes);
    }

    @Override
    public AnimationType startProper(ItemDisplay itemDisplay, boolean repeat) {
        if (itemDisplay.getAnimation() != null) itemDisplay.getAnimation().stop(itemDisplay);
        itemDisplay.setAnimation(this);
        animationTypes.forEach((animationType -> animationType.start(itemDisplay, repeat)));
        return this;
    }

    @Override
    public void stop(ItemDisplay itemDisplay) {
        animationTypes.forEach((animationType -> animationType.stop(itemDisplay)));
    }
}
