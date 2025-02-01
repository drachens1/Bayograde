package org.drachens.events.countries.nonaggression;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;

public record NonAggressionExpireEvent(NonAggressionPact nonAggressionPact) implements Event {
}
