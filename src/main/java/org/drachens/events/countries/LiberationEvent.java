package org.drachens.events.countries;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.Country;

public record LiberationEvent(Country liberated, Country liberator, String type) implements Event {
}
