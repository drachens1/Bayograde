package org.drachens.events.system;

import net.minestom.server.event.Event;
import net.minestom.server.instance.Instance;

public record ResetEvent(Instance instance) implements Event {
}
