package org.drachens.animation;

import net.minestom.server.item.Material;
import org.drachens.dataClasses.other.ItemDisplay;

import java.time.temporal.ChronoUnit;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Animation extends AnimationType {
    private float fps;
    private final int[] frames;
    private final Material item;

    public Animation(float fps, Material item, int[] frames){
        this.fps = fps;
        this.item = item;
        this.frames = frames;
    }
    public AnimationType startProper(ItemDisplay itemDisplay, boolean repeat){
        addTask(itemDisplay,getScheduler().buildTask(new Runnable() {
            int current = 0;
            @Override
            public void run() {
                if (frames.length==current){
                    if (repeat){
                        current = 0;
                    }else{
                        cancelTask(itemDisplay);
                        return;
                    }
                }
                System.out.println(current);
                itemDisplay.setItem(itemBuilder(item, frames[current]));
                current++;
            }
        }).repeat((long) fps, ChronoUnit.SECONDS).schedule());
        return this;
    }
    public void stop(ItemDisplay itemDisplay){
        cancelTask(itemDisplay);
    }
}
