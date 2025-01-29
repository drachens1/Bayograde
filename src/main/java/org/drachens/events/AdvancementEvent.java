package org.drachens.events;

import org.drachens.interfaces.Event;
import org.drachens.player_types.CPlayer;

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
