package org.drachens.events.countries.war;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;

public record StartWarEvent(Country attacker, Country defender, WarJustification warJustification) implements Event { }