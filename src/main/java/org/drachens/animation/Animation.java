package org.drachens.animation;

import lombok.Getter;
import net.minestom.server.item.Material;
import org.drachens.dataClasses.other.ItemDisplay;

import java.time.temporal.ChronoUnit;

import static org.drachens.util.ItemStackUtil.itemBuilder;

@Getter
public class Animation extends AnimationType {
    private final int[] frames;
    private final Material item;
    private final long delayBetween;

    public Animation(long delayBetween, Material item, int[] frames) {
        this.delayBetween = delayBetween;
        this.item = item;
        this.frames = frames;
    }

    @Override
    public AnimationType startProper(ItemDisplay itemDisplay, boolean repeat) {
        if (null != itemDisplay.getAnimation()) itemDisplay.getAnimation().stop(itemDisplay);
        itemDisplay.setAnimation(this);
        addTask(itemDisplay, getScheduler().buildTask(new Runnable() {
            int current;

            @Override
            public void run() {
                if (frames.length == current) {
                    if (repeat) {
                        current = 0;
                    } else {
                        cancelTask(itemDisplay);
                        return;
                    }
                }
                itemDisplay.setItem(itemBuilder(item, frames[current]));
                current++;
            }
        }).repeat(delayBetween, ChronoUnit.MILLIS).schedule());
        return this;
    }

    @Override
    public void stop(ItemDisplay itemDisplay) {
        cancelTask(itemDisplay);
    }
}
