package org.drachens.animation;

import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.timer.Task;
import org.drachens.dataClasses.other.ItemDisplay;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;

public abstract class AnimationType {
    private final SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
    private final HashMap<ItemDisplay, Task> taskHashMap = new HashMap<>();
    private final HashMap<ItemDisplay, Runnable> finishHashMap = new HashMap<>();

    public AnimationType start(ItemDisplay itemDisplay) {
        itemDisplay.setActive(this);
        return startProper(itemDisplay, false);
    }

    public AnimationType start(ItemDisplay itemDisplay, boolean repeat) {
        itemDisplay.setActive(this);
        return startProper(itemDisplay, repeat);
    }

    public AnimationType start(ItemDisplay itemDisplay, boolean repeat, long delay) {
        schedulerManager.buildTask(() -> startProper(itemDisplay, repeat)).delay(delay, ChronoUnit.MILLIS).schedule();
        return null;
    }

    protected abstract AnimationType startProper(ItemDisplay itemDisplay, boolean repeat);

    public void onFinish(ItemDisplay itemDisplay, Runnable runnable) {
        finishHashMap.put(itemDisplay, runnable);
    }

    public abstract void stop(ItemDisplay itemDisplay);

    public void addTask(ItemDisplay itemDisplay, Task task) {
        taskHashMap.put(itemDisplay, task);
    }

    public boolean taskMapContains(ItemDisplay itemDisplay) {
        return taskHashMap.containsKey(itemDisplay);
    }

    public void cancelTask(ItemDisplay itemDisplay) {
        if (taskMapContains(itemDisplay)) {
            taskHashMap.get(itemDisplay).cancel();
            taskHashMap.remove(itemDisplay);
            if (!finishHashMap.containsKey(itemDisplay)) return;
            finishHashMap.get(itemDisplay).run();
            finishHashMap.remove(itemDisplay);
        }
    }

    public Scheduler getScheduler() {
        return schedulerManager;
    }
}
