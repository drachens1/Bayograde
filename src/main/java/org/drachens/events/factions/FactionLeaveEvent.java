package org.drachens.events.factions;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;

public record FactionLeaveEvent(Faction faction, Country country) implements Event {
}

