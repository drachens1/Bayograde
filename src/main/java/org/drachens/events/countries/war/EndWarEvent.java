package org.drachens.events.countries.war;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.Country;

public record EndWarEvent(Country from, Country to) implements Event {
}
