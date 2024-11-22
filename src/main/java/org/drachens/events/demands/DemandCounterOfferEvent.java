package org.drachens.events.demands;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.Event;

public class DemandCounterOfferEvent extends Event {
    private final Country from;
    private final Country to;

    public DemandCounterOfferEvent(Country from, Country to) {
        super(from.getInstance());
        this.to = to;
        this.from = from;
    }

    public Country getFrom() {
        return from;
    }

    public Country getTo() {
        return to;
    }
}
