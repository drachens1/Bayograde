package org.drachens.Manager.defaults.scheduler;

import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.interfaces.Event;

public class ContinentalScheduler {
    private final Class<? extends Event> event;
    private final SchedulerRunnable runnable;
    private final Instance instance;
    private final int intermission;
    private final ContinentalSchedulerManager schedulerManager = ContinentalManagers.schedulerManager;
    private int count = 0;

    private ContinentalScheduler(Create create) {
        this.event = create.event;
        this.runnable = create.runnable;
        this.instance = create.instance;
        this.intermission = create.intermission;
    }

    public Class<? extends Event> getEvent() {
        return event;
    }

    public Instance getInstance() {
        return instance;
    }

    public void AttemptRun(Instance instance, Event event) {
        if (run(instance)) {
            runnable.run(event);
        }
    }

    public boolean run(Instance instance) {
        if (this.instance!=instance)return false;
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

        public Create(Class<? extends Event> event, SchedulerRunnable runnable) {
            this.event = event;
            this.runnable = runnable;
        }

        public Create setDelay(int intermission) {
            this.intermission = intermission;
            return this;
        }

        public ContinentalScheduler schedule() {
            return new ContinentalScheduler(this);
        }

        public Class<? extends Event> getEvent() {
            return event;
        }

        public Instance getInstance() {
            return instance;
        }

        public Create setInstance(Instance instance) {
            this.instance = instance;
            return this;
        }
    }
}
