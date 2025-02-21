package org.drachens.events.countries.warjustification;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;

public record WarJustificationCancelEvent(WarJustification warJustification, Country from) implements Event { }