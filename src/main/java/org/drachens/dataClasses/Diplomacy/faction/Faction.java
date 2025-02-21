package org.drachens.dataClasses.Diplomacy.faction;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InvitesEnum;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.additional.Modifier;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class Faction {
    private final List<Country> members;
    private final List<Country> invites;
    private final Modifier modifier;
    private final CountryDataManager countryDataManager;
    private final FactionChat factionChat;
    private Country leader;
    private String name;
    private Component description;
    private Component nameComponent;

    public Faction(Country leader, String name, Modifier modifier) {
        this.leader = leader;
        this.name = name;
        this.modifier = modifier;
        this.members = new ArrayList<>();
        this.invites = new ArrayList<>();
        countryDataManager = ContinentalManagers.world(leader.getInstance()).countryDataManager();
        countryDataManager.addFaction(this);
        addCountry(leader);
        factionChat = new FactionChat(this);
    }

    public void createDescription() {
        nameComponent = Component.text()
                .append(Component.text()
                        .append(Component.text(name, NamedTextColor.GOLD, TextDecoration.BOLD))
                        .hoverEvent(description)
                        .clickEvent(ClickEvent.runCommand("/faction info " + name))
                )
                .build();
        List<Component> memberComponents = new ArrayList<>();
        members.forEach(member -> memberComponents.add(member.getInfo().getOriginalName()));
        description = Component.text()
                .append(Component.text("_______/", NamedTextColor.BLUE))
                .append(Component.text(name, NamedTextColor.GOLD))
                .append(Component.text("\\_______", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Leader: "))
                .append(leader.getInfo().getOriginalName())
                .appendNewline()
                .append(Component.text("Modifier: "))
                .append(modifier.getName())
                .appendNewline()
                .append(Component.text("Members: "))
                .append(memberComponents)
                .appendNewline()
                .append(Component.text()
                        .append(Component.text("[OPTIONS]",NamedTextColor.GOLD,TextDecoration.BOLD))
                        .hoverEvent(HoverEvent.showText(Component.text("Click to get view the options", NamedTextColor.GRAY)))
                        .clickEvent(ClickEvent.runCommand("/faction options")))
                .build();
    }

    public void addCountry(Country country) {
        if (members.contains(country)) return;
        if (!hasInvited(country)) return;
        removeInvite(country);
        country.addModifier(modifier);
        addMember(country);
        members.forEach(member-> {
            member.loadCountriesDiplomacy(country);
            country.loadCountriesDiplomacy(member);
        });
        members.add(country);
        createDescription();
        country.getInfo().getPlayers().forEach(Player::refreshCommands);
    }

    public void removeCountry(Country country) {
        if (!members.contains(country)) return;
        country.removeModifier(modifier);
        removeMember(country);
        members.forEach(member-> {
            member.loadCountriesDiplomacy(country);
            country.loadCountriesDiplomacy(member);
        });
        members.remove(country);
        createDescription();
        country.getInfo().getPlayers().forEach(Player::refreshCommands);
    }

    protected abstract void addMember(Country country);

    protected abstract void removeMember(Country country);

    public String getStringName() {
        return name;
    }

    public void rename(String newName) {
        countryDataManager.renameFaction(name, newName, this);
        name = newName;
    }

    public abstract Component getName();

    public void delete() {
        Instance instance = leader.getInstance();
        CountryDataManager countryDataManager = ContinentalManagers.world(instance).countryDataManager();
        countryDataManager.removeFaction(this);
    }

    public void addToInvites(Country country) {
        invites.add(country);
    }

    public boolean hasInvited(Country country) {
        return invites.contains(country);
    }

    public void removeInvite(Country country) {
        invites.remove(country);
        country.getDiplomacy().removeInvite(InvitesEnum.faction,getStringName());
    }

    public void sendMessage(Component message) {
        members.forEach(member -> member.sendMessage(message));
    }

    public boolean isLeader(Country country) {
        return leader == country;
    }

    public boolean containsCountry(Country country) {
        return members.contains(country);
    }
}