package org.drachens.events.countries.war;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class UnconditionalSurrenderEvent extends Event {
    private final Country from;
    private final Country to;

    public UnconditionalSurrenderEvent(Country from, Country to) {
        super(from.getInstance());
        this.from = from;
        this.to = to;
    }

    public Country getFrom() {
        return from;
    }

    public Country getTo() {
        return to;
    }
}
