package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.drachens.dataClasses.Countries.Country;

import java.util.ArrayList;
import java.util.List;

public class Factions {
    private final Country creator;
    private String name;
    private Component description;
    private List<Country> members;
    private Component nameComponent;
    private final FactionType factionType;
    public Factions(Country creator, String name, FactionType factionType) {
        this.factionType = factionType;
        this.creator = creator;
        this.name = name;
        this.members = new ArrayList<>();
        members.add(creator);
        createDescription();
        nameComponent = Component.text()
                .append(Component.text()
                        .append(Component.text(name,NamedTextColor.GOLD, TextDecoration.BOLD))
                        .hoverEvent(description)
                        .clickEvent(ClickEvent.runCommand("/faction info "+name))
                )
                .build();
        factionType.setFactions(this);
    }
    public void createDescription(){
        description = Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text(getName(), NamedTextColor.GOLD))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(creator.getNameComponent())
                .build();
    }
    public String getName() {
        return name;
    }
    public Component getDescription(){
        return description;
    }

    public Country getCreator() {
        return creator;
    }

    public List<Country> getMembers() {
        return members;
    }

    public void addMember(Country country) {
        if (members.contains(country))return;
        members.add(country);
        factionType.countryJoins(country);
    }

    public void removeMember(Country country) {
        if (!members.contains(country))return;
        members.remove(country);
    }

    public void rename(String newName) {
        name = newName;
    }
    public Component getNameComponent(){
        return nameComponent;
    }
    public FactionType getFactionType(){
        return factionType;
    }
}
