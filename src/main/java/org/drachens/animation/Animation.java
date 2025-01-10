package org.drachens.animation;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.other.ItemDisplay;

import java.time.temporal.ChronoUnit;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Animation extends AnimationType {
    private final int[] frames;
    private final Material item;
    private final long delayBetween;

    public Animation(long delayBetween, Material item, int[] frames) {
        this.delayBetween = delayBetween;
        this.item = item;
        this.frames = frames;
    }

    public AnimationType startProper(ItemDisplay itemDisplay, boolean repeat) {
        if (itemDisplay.getAnimation()!=null)itemDisplay.getAnimation().stop(itemDisplay);
        itemDisplay.setAnimation(this);
        addTask(itemDisplay, getScheduler().buildTask(new Runnable() {
            int current = 0;

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

    public void stop(ItemDisplay itemDisplay) {
        cancelTask(itemDisplay);
    }
}
