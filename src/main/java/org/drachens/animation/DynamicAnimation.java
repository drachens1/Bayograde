package org.drachens.animation;

import net.minestom.server.MinecraftServer;
import net.minestom.server.item.Material;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.Task;
import org.drachens.dataClasses.other.ItemDisplay;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import static org.drachens.util.ItemStackUtil.itemBuilder;

public class DynamicAnimation extends AnimationType {
    private final Material item;
    private final int[][] frames;
    private final Scheduler scheduler = MinecraftServer.getSchedulerManager();
    private final HashMap<ItemDisplay, Task> taskHashMap = new HashMap<>();

    public DynamicAnimation(Material item, int[][] frames) {
        this.item = item;
        this.frames = frames;
    }

    public AnimationType startProper(ItemDisplay itemDisplay, boolean repeat) {
        if (itemDisplay.getAnimation() != null) itemDisplay.getAnimation().stop(itemDisplay);
        itemDisplay.setAnimation(this);
        scheduleNextFrame(itemDisplay, 0, repeat);
        return this;
    }

    private void scheduleNextFrame(ItemDisplay itemDisplay, int current, boolean repeat) {
        long delay = getDelay(current);

        taskHashMap.put(itemDisplay, scheduler.buildTask(() -> {
            itemDisplay.setItem(itemBuilder(item, frames[current][1]));

            int nextFrame = current + 1;
            if (nextFrame >= frames.length) {
                if (repeat) {
                    scheduleNextFrame(itemDisplay, 0, true);
                    return;
                } else {
                    taskHashMap.remove(itemDisplay);
                    return;
                }
            }

            scheduleNextFrame(itemDisplay, nextFrame, repeat);
        }).delay(delay, ChronoUnit.MILLIS).schedule());
    }

    public void stop(ItemDisplay itemDisplay) {
        Task task = taskHashMap.get(itemDisplay);
        if (task != null) {
            task.cancel();
            taskHashMap.remove(itemDisplay);
            itemDisplay.setItem(itemBuilder(item, frames[0][1]));
        }
    }

    private long getDelay(int current) {
        if (0 <= current - 1) {
            return frames[current - 1][0];
        } else {
            return frames[frames.length - 1][0];
        }
    }
}
