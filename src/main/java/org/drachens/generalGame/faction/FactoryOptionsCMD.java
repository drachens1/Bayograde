package org.drachens.generalGame.faction;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

public class FactoryOptionsCMD extends Command {
    public FactoryOptionsCMD() {
        super("options");

        setDefaultExecutor((sender, context)->{
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            if (null == country) return;
            List<Component> comps = new ArrayList<>();
            comps.add(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(Component.text("Faction-Options"))
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .build());
            if (country.isInAFaction()){
                if (country.isInAnEconomicFaction()){
                    EconomyFactionType economicFaction = country.getEconomy().getEconomyFactionType();
                    String name = economicFaction.getStringName();
                    comps.add(Component.text()
                            .appendNewline()
                            .append(economicFaction.getNameComponent())
                            .append(Component.text(" - Economy:"))
                            .appendNewline()
                            .build());
                    if (economicFaction.isLeader(country)) {
                        comps.add(Component.text()
                                .append(Component.text("Manage: "))
                                .appendNewline()
                                .append(Component.text()
                                        .append(Component.text(" [DELETE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to delete this faction", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.runCommand("/faction manage delete " + name)))
                                .append(Component.text()
                                        .append(Component.text(" [INVITE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt for inviting someone", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.suggestCommand("/faction manage invite" + name + ' ')))
                                .append(Component.text()
                                        .append(Component.text(" [KICK] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt for kicking someone", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.suggestCommand("/faction manage kick " + name + ' ')))
                                .appendNewline()
                                .append(Component.text()
                                        .append(Component.text(" [RENAME] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt for setting the name", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.suggestCommand("/faction manage rename " + name + ' ')))
                                .append(Component.text()
                                        .append(Component.text(" [SET-LEADER] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt for setting the leader", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.suggestCommand("/faction manage set-leader " + name + ' ')))
                                .appendNewline()
                                .build());
                    }
                    comps.add(Component.text()
                            .append(Component.text()
                                    .append(Component.text(" [LEAVE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to leave the faction", NamedTextColor.GRAY)))
                                    .clickEvent(ClickEvent.runCommand("/faction leave "+name)))
                            .append(Component.text()
                                    .append(Component.text(" [CHAT] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to start or stop chatting", NamedTextColor.GRAY)))
                                    .clickEvent(ClickEvent.runCommand("/faction chat "+name)))
                            .appendNewline()
                            .build());
                }

                if (country.isInAMilitaryFaction()){
                    MilitaryFactionType militaryFaction = country.getEconomy().getMilitaryFactionType();
                    String name = militaryFaction.getStringName();
                    comps.add(Component.text()
                            .append(militaryFaction.getNameComponent())
                            .append(Component.text(" - Military:"))
                            .appendNewline()
                            .build());
                    if (militaryFaction.isLeader(country)) {
                        comps.add(Component.text()
                                .append(Component.text("Manage: "))
                                .appendNewline()
                                .append(Component.text()
                                        .append(Component.text(" [DELETE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to delete this faction", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.runCommand("/faction manage delete " + name)))
                                .append(Component.text()
                                        .append(Component.text(" [INVITE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt for inviting someone", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.suggestCommand("/faction manage invite" + name + ' ')))
                                .append(Component.text()
                                        .append(Component.text(" [KICK] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt for kicking someone", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.suggestCommand("/faction manage kick " + name + ' ')))
                                .appendNewline()
                                .append(Component.text()
                                        .append(Component.text(" [RENAME] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt for setting the name", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.suggestCommand("/faction manage rename " + name + ' ')))
                                .append(Component.text()
                                        .append(Component.text(" [SET-LEADER] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                        .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt for setting the leader", NamedTextColor.GRAY)))
                                        .clickEvent(ClickEvent.suggestCommand("/faction manage set-leader " + name + ' ')))
                                .appendNewline()
                                .build());
                    }
                    comps.add(Component.text()
                            .append(Component.text()
                                    .append(Component.text(" [LEAVE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to leave the faction", NamedTextColor.GRAY)))
                                    .clickEvent(ClickEvent.runCommand("/faction leave "+name)))
                            .append(Component.text()
                                    .append(Component.text(" [CHAT] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to start or stop chatting", NamedTextColor.GRAY)))
                                    .clickEvent(ClickEvent.runCommand("/faction chat "+name)))
                            .append(Component.text()
                                    .append(Component.text(" [INFO] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to start or stop chatting", NamedTextColor.GRAY)))
                                    .clickEvent(ClickEvent.runCommand("/faction info "+name)))
                            .appendNewline()
                            .build());
                }
            }
            if (!country.isInAllFactions()) {
                comps.add(Component.text()
                        .appendNewline()
                        .append(Component.text("Other:"))
                        .appendNewline()
                        .append(Component.text()
                                .append(Component.text(" [JOIN] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt to join a faction", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.runCommand("/faction join ")))
                        .append(Component.text()
                                .append(Component.text(" [CREATE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to get a prompt to create a faction", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.suggestCommand("/faction create ")))
                        .build());
            }

            p.sendMessage(Component.text()
                    .append(comps)
                    .build());
        });
    }

}
