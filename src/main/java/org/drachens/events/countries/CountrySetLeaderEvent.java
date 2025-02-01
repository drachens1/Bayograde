package org.drachens.events.countries;

import net.minestom.server.event.Event;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

public record CountrySetLeaderEvent(Country country, CPlayer newLeader) implements Event {
}