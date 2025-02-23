package org.drachens.events;

import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;

public record CustomTick(Instance instance) implements Event {}
