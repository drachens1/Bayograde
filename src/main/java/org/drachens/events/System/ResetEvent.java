package org.drachens.events.System;

import net.minestom.server.instance.Instance;
import org.drachens.interfaces.Event;

public class ResetEvent extends Event {
    private final Instance instance;

    public ResetEvent(Instance instance) {
        super(instance);
        this.instance = instance;
    }

    public Instance getInstance() {
        return instance;
    }
}
