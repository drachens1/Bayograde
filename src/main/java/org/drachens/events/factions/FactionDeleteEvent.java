package org.drachens.events.factions;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;

public record FactionDeleteEvent(Country deleter, Faction deletedFaction) implements Event {
}
