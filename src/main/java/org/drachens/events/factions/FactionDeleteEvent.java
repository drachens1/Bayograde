package org.drachens.events.factions;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;

public record FactionDeleteEvent(Country deleter, Factions deletedFaction) implements Event {
}
