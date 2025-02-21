package org.drachens.events.countries;

import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.countryClass.Country;

public record CountryLeaveEvent(Country left, Player p) implements Event {
}