package org.drachens.events;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Province;

public record CaptureBlockEvent(Country attacker, Country defender, Province attacked) implements Event { }
