package org.drachens.Manager.defaults.scheduler;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.interfaces.Event;

import java.util.HashMap;

public class ContinentalScheduler {
    private final Class<? extends Event> event;
    private final SchedulerRunnable runnable;
    private boolean global;
    private final Instance instance;
    private final HashMap<Instance, Integer> globalDelay = new HashMap<>();
    private final int intermission;
    private final boolean repeat;
    private final ContinentalSchedulerManager schedulerManager = ContinentalManagers.schedulerManager;
    private int count = 0;

    private ContinentalScheduler(Create create) {
        this.event = create.event;
        this.runnable = create.runnable;
        this.instance = create.instance;
        this.intermission = create.intermission;
        this.repeat = create.repeat;
        this.global = instance == null;
    }

    public Class<? extends Event> getEvent() {
        return event;
    }

    public Instance getInstance() {
        return instance;
    }

    public boolean isGlobal() {
        return global;
    }

    public void AttemptRun(Instance instance, Event event) {
        if (run(instance)) {
            runnable.run(event);
        }
    }

    public boolean run(Instance instance) {
        if (global) {
            if (!globalDelay.containsKey(instance)) globalDelay.put(instance, 0);
            if (globalDelay.get(instance) >= intermission) {
                globalDelay.put(instance, 0);
                return true;
            }
            int i = globalDelay.get(instance);
            i++;
            globalDelay.put(instance, i);
            return false;
        }
        if (count >= intermission) {
            count = 0;
            return true;
        }
        count++;
        return false;
    }

    public void stop() {
        schedulerManager.unregister(this);
    }

    public static class Create {
        private final Class<? extends Event> event;
        private final SchedulerRunnable runnable;
        private Instance instance;
        private int intermission;
        private boolean repeat = false;

        public Create(Class<? extends Event> event, SchedulerRunnable runnable) {
            this.event = event;
            this.runnable = runnable;
        }

        // Set the instance
        public Create setInstance(Instance instance) {
            this.instance = instance;
            return this;
        }

        // Set delay (intermission)
        public Create setDelay(int intermission) {
            this.intermission = intermission;
            return this;
        }

        // Set repeat flag
        public Create repeat() {
            this.repeat = true;
            return this;
        }

        public ContinentalScheduler schedule() {
            return new ContinentalScheduler(this);
        }

        public Class<? extends Event> getEvent() {
            return event;
        }

        public SchedulerRunnable getRunnable() {
            return runnable;
        }

        public Instance getInstance() {
            return instance;
        }

        public int getIntermission() {
            return intermission;
        }

        public boolean isRepeat() {
            return repeat;
        }
    }
}
