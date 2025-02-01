package org.drachens.events.countries.demands;


import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Demand;

public record DemandAcceptedEvent(Demand demand, Country from, Country to) implements Event {}