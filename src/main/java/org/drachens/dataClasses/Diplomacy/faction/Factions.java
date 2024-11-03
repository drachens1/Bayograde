package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.Country;

import java.util.ArrayList;
import java.util.List;

public abstract class Factions {
    private final Country creator;
    private String name;
    private Component description;
    private final List<Country> members;
    private Component nameComponent;
    public Factions(Country creator, String name) {
        this.creator = creator;
        this.name = name;
        this.members = new ArrayList<>();
        addMember(creator);
        createDescription();
        ContinentalManagers.world(creator.getInstance()).countryDataManager().addFaction(this);
        nameComponent = Component.text()
                .append(Component.text()
                        .append(Component.text(name,NamedTextColor.GOLD, TextDecoration.BOLD))
                        .hoverEvent(description)
                        .clickEvent(ClickEvent.runCommand("/faction info "+name))
                )
                .build();
    }
    public void createDescription(){
        nameComponent = Component.text()
                .append(Component.text()
                        .append(Component.text(name,NamedTextColor.GOLD, TextDecoration.BOLD))
                        .hoverEvent(description)
                        .clickEvent(ClickEvent.runCommand("/faction info "+name))
                )
                .build();
        description = Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text(name, NamedTextColor.GOLD))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(creator.getNameComponent())
                .build();
    }

    public Country getCreator() {
        return creator;
    }

    public List<Country> getMembers() {
        return members;
    }

    protected void addMember(Country country) {
        if (members.contains(country))return;
        members.add(country);
        country.createInfo();
    }

    protected void removeMember(Country country) {
        if (!members.contains(country))return;
        members.remove(country);
        country.createInfo();
    }
    public String getStringName(){
        return name;
    }

    public void rename(String newName) {
        name = newName;
    }
    public Component getNameComponent(){
        return nameComponent;
    }

    public abstract Component getName();

    public abstract void setFactions(Factions factions);
    public abstract void countryJoins(Country country);
    public abstract void countryLeaves(Country country);
    public Country getLeader(){
        return creator;
    }
    public void delete(){
        Instance instance = creator.getInstance();
        CountryDataManager countryDataManager = ContinentalManagers.world(instance).countryDataManager();
        countryDataManager.removeFaction(this);
    }
}
