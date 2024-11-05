package org.drachens.dataClasses.Diplomacy.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Modifier;
import org.drachens.events.Factions.FactionInviteEvent;
import org.drachens.events.Factions.FactionSetLeaderEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class Factions {
    private Country leader;
    private String name;
    private Component description;
    private final List<Country> members;
    private final List<Country> invites;
    private Component nameComponent;
    private final Modifier modifier;
    public Factions(Country leader, String name, Modifier modifier) {
        this.leader = leader;
        this.name = name;
        this.modifier = modifier;
        this.members = new ArrayList<>();
        this.invites = new ArrayList<>();
        ContinentalManagers.world(leader.getInstance()).countryDataManager().addFaction(this);
        addCountry(leader);
    }
    public void createDescription(){
        nameComponent = Component.text()
                .append(Component.text()
                        .append(Component.text(name,NamedTextColor.GOLD, TextDecoration.BOLD))
                        .hoverEvent(description)
                        .clickEvent(ClickEvent.runCommand("/faction info "+name))
                )
                .build();
        List<Component> memberComponents = new ArrayList<>();
        members.forEach(member->memberComponents.add(member.getNameComponent()));
        description = Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text(name, NamedTextColor.GOLD))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(leader.getNameComponent())
                .appendNewline()
                .append(Component.text("Modifier: "))
                .append(modifier.getName())
                .appendNewline()
                .append(Component.text("Members: "))
                .append(memberComponents)
                .build();
    }

    public Country getLeader() {
        return leader;
    }

    public List<Country> getMembers() {
        return members;
    }

     public void addCountry(Country country) {
        if (members.contains(country))return;
        members.add(country);
        country.addModifier(modifier);
        createDescription();
        addMember(country);
    }

    public void removeCountry(Country country) {
        if (!members.contains(country))return;
        members.remove(country);
        country.removeModifier(modifier);
        removeMember(country);
        createDescription();
    }
    protected abstract void addMember(Country country);
    protected abstract void removeMember(Country country);
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
    public void delete(){
        Instance instance = leader.getInstance();
        CountryDataManager countryDataManager = ContinentalManagers.world(instance).countryDataManager();
        countryDataManager.removeFaction(this);
    }
    public void invite(Country country){
        invites.add(country);
        EventDispatcher.call(new FactionInviteEvent(country,this));
    }
    public boolean isInvited(Country country){
        return invites.contains(country);
    }
    public void removeInvite(Country country){
        invites.remove(country);
    }
    public void sendMessage(Component message){
        members.forEach(member->member.sendMessage(message));
    }
    public void sendActionBar(Component message){
        members.forEach(member->member.sendActionBar(message));
    }
    public boolean isLeader(Country country){
        return leader==country;
    }
    public void setLeader(Country country){
        this.leader = country;
        EventDispatcher.call(new FactionSetLeaderEvent(this,country));
    }
    public Modifier getModifier(){
        return modifier;
    }
    public Component getDescription(){
        return description;
    }
}
