package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Countries.Country;

public interface FactionType {
    Component getName();
    void setFactions(Factions factions);
    void countryJoins(Country country);
    void countryLeaves(Country country);
}
