package org.drachens.events.System;

import net.minestom.server.event.Event;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.instance.Instance;

public class ResetEvent implements Event, CancellableEvent {
    private final Instance instance;
    public ResetEvent(Instance instance) {
        this.instance = instance;
    }
    public Instance getInstance(){
        return instance;
    }
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean b) {

    }
}
