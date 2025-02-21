package org.drachens.events.countries.war;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.countryClass.Country;

public record UnconditionalSurrenderEvent(Country from, Country to) implements Event {
}
