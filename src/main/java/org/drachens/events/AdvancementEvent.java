package org.drachens.events;

import dev.ng5m.CPlayer;
import org.drachens.interfaces.Event;

public class AdvancementEvent extends Event {
    private final String name;
    private final CPlayer p;

    public AdvancementEvent(CPlayer p, String identifier) {
        super(p.getInstance());
        this.p = p;
        this.name = identifier;
    }

    public CPlayer getPlayer() {
        return p;
    }

    public String getName() {
        return name;
    }
}
