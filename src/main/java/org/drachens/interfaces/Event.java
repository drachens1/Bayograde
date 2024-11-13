package org.drachens.interfaces;

import net.minestom.server.event.trait.CancellableEvent;

public abstract class Event implements net.minestom.server.event.Event, CancellableEvent {
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
