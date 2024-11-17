package org.drachens.events.demands;


import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.interfaces.Event;

public class DemandAcceptedEvent extends Event {
    private final Demand demand;
    private final Country from;
    private final Country to;

    public DemandAcceptedEvent(Demand demand, Country from, Country to) {
        this.demand = demand;
        this.from = from;
        this.to = to;
    }

    public Demand getDemand() {
        return demand;
    }

    public Country getFrom() {
        return from;
    }

    public Country getTo() {
        return to;
    }

}
