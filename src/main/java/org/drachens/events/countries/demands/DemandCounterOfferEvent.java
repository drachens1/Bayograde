package org.drachens.events.countries.demands;

import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.interfaces.Event;

public class DemandCounterOfferEvent extends Event {
    private final Country from;
    private final Country to;
    private final Demand original;

    public DemandCounterOfferEvent(Country from, Country to, Demand original) {
        super(from.getInstance());
        this.to = to;
        this.from = from;
        this.original=original;
    }

    public Country getFrom() {
        return from;
    }

    public Country getTo() {
        return to;
    }

    public Demand getOriginal(){
        return original;
    }
}
