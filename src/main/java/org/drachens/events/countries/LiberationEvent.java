package org.drachens.events.countries;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class LiberationEvent extends Event {
    private final Country liberated;
    private final Country liberator;
    private final String type;

    public LiberationEvent(Country liberated, Country liberator, String type) {
        super(liberator.getInstance());
        this.liberated=liberated;
        this.liberator=liberator;
        this.type=type;
    }

    public Country getLiberated() {
        return liberated;
    }

    public Country getLiberator() {
        return liberator;
    }

    public String getType(){
        return type;
    }
}
