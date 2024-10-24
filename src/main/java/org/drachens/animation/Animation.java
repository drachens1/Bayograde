package org.drachens.animation;

import net.minestom.server.MinecraftServer;
import net.minestom.server.item.Material;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;
import org.drachens.dataClasses.other.ItemDisplay;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class Animation implements AnimationType {
    private float fps;
    private final int[] frames;
    private final Material item;
    private final SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
    private final HashMap<ItemDisplay, Task> taskHashMap = new HashMap<>();
    public Animation(float fps, Material item, int[] frames){
        this.fps = fps;
        this.item = item;
        this.frames = frames;
    }
    public void start(ItemDisplay itemDisplay, boolean repeat){
        taskHashMap.put(itemDisplay,schedulerManager.buildTask(new Runnable() {
            int current = 0;
            @Override
            public void run() {
                if (frames.length==current){
                    if (repeat){
                        current = 0;
                    }else
                        taskHashMap.get(itemDisplay).cancel();
                }
                itemDisplay.setItem(itemBuilder(item, frames[current]));
                current++;
            }
        }).repeat((long) fps, ChronoUnit.SECONDS).schedule());
    }
    public void stop(ItemDisplay itemDisplay){
        if (taskHashMap.containsKey(itemDisplay)){
            taskHashMap.get(itemDisplay).cancel();
        }
    }
}
