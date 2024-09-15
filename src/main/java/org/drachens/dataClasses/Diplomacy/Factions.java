package org.drachens.dataClasses.Diplomacy;

import org.drachens.dataClasses.Countries.Country;

import java.util.List;

public class Factions {
    private final Country creator;
    private String name;
    private List<Country> members;

    public Factions(Country creator, String name) {
        this.creator = creator;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Country getCreator() {
        return creator;
    }

    public List<Country> getMembers() {
        return members;
    }

    public void addMember(Country country) {
        members.add(country);
    }

    public void removeMember(Country country) {
        members.remove(country);
    }

    public void rename(String newName) {
        name = newName;
    }
}
