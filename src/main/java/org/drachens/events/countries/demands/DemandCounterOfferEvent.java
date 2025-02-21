package org.drachens.events.countries.demands;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Demand;

public record DemandCounterOfferEvent(Country from, Country to, Demand original) implements Event {
}
