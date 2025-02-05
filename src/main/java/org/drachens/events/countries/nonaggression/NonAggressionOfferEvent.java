package org.drachens.events.countries.nonaggression;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;

public record NonAggressionOfferEvent(NonAggressionPact nonAggressionPact) implements Event { }
