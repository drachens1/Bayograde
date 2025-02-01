package org.drachens.events.factions;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;

public record FactionCreateEvent(Country creator, Factions newFaction) implements Event {
}
