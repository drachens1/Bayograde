package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import org.drachens.dataClasses.Armys.Troop;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.other.Clientside;

import java.util.ArrayList;
import java.util.List;

public class MilitaryFactionType extends Factions {


    public MilitaryFactionType(Country creator, String name) {
        super(creator, name);
    }

    @Override
    public Component getName() {
        return null;
    }

    @Override
    public void setFactions(Factions factions) {

    }

    @Override
    public void countryJoins(Country country) {
        addMember(country);
        getMembers().forEach(member->country.loadClientsides(member.getAlliedTroopClientsides()));
    }

    @Override
    public void countryLeaves(Country country) {
        removeMember(country);
        getMembers().forEach(member->country.unloadClientsides(member.getAlliedTroopClientsides()));
    }

    public void addTroop(Troop troop, Country country){
        List<Country> members = new ArrayList<>(getMembers());
        members.remove(country);
        Clientside clientside = troop.getAlly();
        members.forEach(member->member.loadClientside(clientside));
    }
    public void removeTroop(Troop troop, Country country){
        List<Country> members = new ArrayList<>(getMembers());
        members.remove(country);
        Clientside clientside = troop.getAlly();
        members.forEach(member->member.unloadClientside(clientside));
    }
}
