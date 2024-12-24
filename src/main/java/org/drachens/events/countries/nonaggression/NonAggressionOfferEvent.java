package org.drachens.events.countries.nonaggression;

import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.interfaces.Event;

public class NonAggressionOfferEvent extends Event {
    private final NonAggressionPact nonAggressionPact;
    public NonAggressionOfferEvent(NonAggressionPact nonAggressionPact) {
        super(nonAggressionPact.getFrom().getInstance());
        this.nonAggressionPact=nonAggressionPact;
    }
    public NonAggressionPact getNonAggressionPact(){
        return nonAggressionPact;
    }
}
