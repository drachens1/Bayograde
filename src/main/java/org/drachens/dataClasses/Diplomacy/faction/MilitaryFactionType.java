package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.additional.Modifier;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.temporary.troops.TroopCountry;

import java.util.ArrayList;
import java.util.List;

public class MilitaryFactionType extends Factions {
    public MilitaryFactionType(Country creator, String name) {
        super(creator, name, new Modifier.create(Component.text(name, NamedTextColor.RED),"military_faction").build());
    }

    @Override
    public Component getName() {
        return null;
    }

    @Override
    public void addMember(Country country) {
        if (country instanceof TroopCountry troopCountry)
            getMembers().forEach(member -> {
                TroopCountry troopCountry1 = (TroopCountry) member;
                troopCountry.loadClientsides(troopCountry1.getAlliedTroopClientsides());
            });
    }

    @Override
    public void removeMember(Country country) {
        if (country instanceof TroopCountry troopCountry)
            getMembers().forEach(member -> {
                TroopCountry troopCountry1 = (TroopCountry) member;
                troopCountry.unloadClientsides(troopCountry1.getAlliedTroopClientsides());
            });
    }

    public void addTroop(Troop troop, Country country) {
        List<Country> members = new ArrayList<>(getMembers());
        members.remove(country);
        Clientside clientside = troop.getAlly();
        members.forEach(member -> member.loadClientside(clientside));
    }

    public void removeTroop(Troop troop, Country country) {
        List<Country> members = new ArrayList<>(getMembers());
        members.remove(country);
        Clientside clientside = troop.getAlly();
        members.forEach(member -> member.unloadClientside(clientside));
    }
}
