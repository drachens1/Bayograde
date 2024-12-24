package org.drachens.events.countries.nonaggression;

import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.interfaces.Event;

public class NonAggressionAcceptedEvent extends Event {
    private final NonAggressionPact nonAggressionPact;
    public NonAggressionAcceptedEvent(NonAggressionPact nonAggressionPact) {
        super(nonAggressionPact.getFrom().getInstance());
        this.nonAggressionPact=nonAggressionPact;
    }
    public NonAggressionPact getNonAggressionPact(){
        return nonAggressionPact;
    }
}
