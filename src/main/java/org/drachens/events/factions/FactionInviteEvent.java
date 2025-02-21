package org.drachens.events.factions;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;

public record FactionInviteEvent(Country invited, Faction faction) implements Event { }