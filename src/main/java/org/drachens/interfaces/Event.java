package org.drachens.interfaces;

import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.instance.Instance;

public abstract class Event implements net.minestom.server.event.Event, CancellableEvent {
    private final Instance instance;
    private boolean cancelled = false;

    public Event(Instance instance) {
        this.instance = instance;
    }

    public Instance getInstance() {
        return instance;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
