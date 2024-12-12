package org.drachens.interfaces;

import dev.ng5m.CPlayer;
import net.minestom.server.instance.Instance;

public class AdvancementEvent extends Event{
    private final String name;
    private final CPlayer p;
    public AdvancementEvent(Instance instance, CPlayer p, String identifier) {
        super(instance);
        this.p=p;
        this.name=identifier;
    }
    public CPlayer getPlayer(){
        return p;
    }
    public String getName(){
        return name;
    }
}
