package org.drachens.Manager.defaults.scheduler;

import org.drachens.interfaces.Event;

@FunctionalInterface
public interface SchedulerRunnable {
    void run(Event e);
}
