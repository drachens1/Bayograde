package org.drachens.Manager.defaults;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.events.Countries.CountryCoopPlayerEvent;
import org.drachens.events.Countries.CountrySetLeaderEvent;
import org.drachens.events.Factions.*;
import org.drachens.events.StartWarEvent;
import org.drachens.events.System.ResetEvent;
import org.drachens.events.System.StartGameEvent;

import java.util.Objects;

import static org.drachens.util.KyoriUtil.getPrefixes;
import static org.drachens.util.Messages.broadcast;
import static org.drachens.util.Messages.globalBroadcast;
import static org.drachens.util.ServerUtil.cooldown;
import static org.drachens.util.ServerUtil.playerHasCooldown;

public class MessageManager {
    private final Component system = getPrefixes("system");
    private final Component country = getPrefixes("country");
    private final Component factionPref = getPrefixes("faction");

    public MessageManager() {
        if (system == null) {
            System.err.println("system null!");
            return;
        }
        if (country == null) {
            System.err.println("Country null");
            return;
        }
        if (factionPref == null) {
            System.err.println("Faction null");
            return;
        }

        Component neutralComponent = Component.text()
                .append(Component.text("_________/", NamedTextColor.BLUE))
                .append(Component.text("Neutral", NamedTextColor.GOLD))
                .append(Component.text("\\_________\n", NamedTextColor.BLUE))
                .append(Component.text("Leader: \n"))
                .build();

        Component gameOver = Component.text()
                .append(system)
                .append(Component.text("Game Over", NamedTextColor.GREEN))
                .build();

        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();

        globEHandler.addListener(StartGameEvent.class, e -> {
            e.getVotingOption().getMapGenerator().generate(e.getInstance(), e.getVotingOption());
            broadcast(Component.text()
                    .append(system)
                    .append(Component.text(e.getVotingOption().getName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                    .append(Component.text(" has won!!", NamedTextColor.GREEN))
                    .build(), e.getInstance());
        });

        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> {
            Province p = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(new Pos(e.getBlockPosition()));
            if (p == null) globalBroadcast("NULLl");
            if (!e.getPlayer().isSneaking() || playerHasCooldown(e.getPlayer()) || !Objects.requireNonNull(p).isCapturable())
                return;
            cooldown(e.getPlayer());
            if (p.getOccupier() == null) {
                e.getPlayer().sendMessage(neutralComponent);
                return;
            }
            Country c = p.getOccupier();
            e.getPlayer().sendMessage(c.getDescription());
        });

        globEHandler.addListener(ResetEvent.class, e -> broadcast(gameOver, e.getInstance()));

        globEHandler.addListener(StartWarEvent.class, e -> {
            Country defender = e.getDefender();
            Country attacker = e.getAggressor();
            defender.addWar(attacker);
            attacker.addWar(defender);
            broadcast(Component.text()
                            .append(country)
                            .append(attacker.getNameComponent())
                            .append(Component.text(" started a war with ", NamedTextColor.RED))
                            .append(defender.getNameComponent())
                            .build(),
                    defender.getCapital().getInstance());
        });

        globEHandler.addListener(FactionCreateEvent.class, e -> {
            Country creator = e.getCreator();
            Factions factions = e.getNewFaction();
            broadcast(Component.text()
                            .append(factionPref)
                            .append(creator.getNameComponent())
                            .append(Component.text(" has created ", NamedTextColor.GREEN))
                            .append(factions.getNameComponent())
                            .build(),
                    creator.getCapital().getInstance());
        });

        globEHandler.addListener(FactionDeleteEvent.class, e -> {
            Country deleter = e.getDeleter();
            Factions factions = e.getDeletedFaction();
            broadcast(Component.text()
                            .append(factionPref)
                            .append(deleter.getNameComponent())
                            .append(Component.text(" has deleted ", NamedTextColor.GREEN))
                            .append(factions.getNameComponent())
                            .build(),
                    deleter.getCapital().getInstance());
        });

        globEHandler.addListener(FactionJoinEvent.class, e -> {
            Country creator = e.country();
            broadcast(Component.text()
                            .append(factionPref)
                            .append(creator.getNameComponent())
                            .append(Component.text(" has joined ", NamedTextColor.GREEN))
                            .append(e.faction().getNameComponent())
                            .build(),
                    creator.getCapital().getInstance());
        });

        globEHandler.addListener(FactionInviteEvent.class, e -> {
            Factions factions = e.getFaction();
            Country invited = e.getInvited();
            factions.sendMessage(Component.text()
                    .append(factionPref)
                    .append(invited.getNameComponent())
                    .append(Component.text(" has been invited to the faction", NamedTextColor.GREEN))
                    .build());
            invited.sendMessage(Component.text()
                    .append(factionPref)
                    .append(Component.text()
                            .append(Component.text(" you have been invited to join", NamedTextColor.GREEN))
                            .clickEvent(ClickEvent.runCommand("faction accept " + factions.getStringName()))
                            .hoverEvent(HoverEvent.showText(Component.text("Click to join the faction", NamedTextColor.GOLD)))
                            .build())
                    .append(factions.getName())

                    .build());
        });

        globEHandler.addListener(FactionKickEvent.class, e -> {
            Factions factions = e.getFaction();
            Country country = e.getCountry();
            factions.sendMessage(Component.text()
                    .append(factionPref)
                    .append(country.getNameComponent())
                    .append(Component.text(" was kicked from the faction", NamedTextColor.RED))
                    .build());
            country.sendMessage(Component.text()
                    .append(factionPref)
                    .append(Component.text("You were kicked from "))
                    .append(factions.getName())
                    .build());
        });

        globEHandler.addListener(FactionSetLeaderEvent.class, e -> {
            Factions factions = e.faction();
            Country country = e.country();
            factions.sendMessage(Component.text()
                    .append(factionPref)
                    .append(country.getNameComponent())
                    .append(Component.text(" has became the leader of the faction", NamedTextColor.GREEN))
                    .build());
            country.sendMessage(Component.text()
                    .append(factionPref)
                    .append(Component.text("assigned you as its leader "))
                    .append(factions.getName())
                    .build());
        });

        globEHandler.addListener(CountrySetLeaderEvent.class, e -> e.country().sendMessage(Component.text()
                .append(country)
                .append(Component.text("The leader is now ", NamedTextColor.GREEN))
                .append(Component.text(e.newLeader().getUsername()))
                .build()));

        globEHandler.addListener(CountryCoopPlayerEvent.class, e -> {
            CPlayer cPlayer = e.p();
            Country country = e.inviter();
            cPlayer.sendMessage(Component.text()
                    .append(this.country)
                    .append(Component.text("You have been invited to join ", NamedTextColor.GREEN))
                    .append(country.getNameComponent())
                    .build());
            country.sendMessage(Component.text()
                    .append(this.country)
                    .append(Component.text(cPlayer.getUsername()))
                    .append(Component.text(" has been invited to join the faction", NamedTextColor.GREEN))
                    .build());
        });
    }
}
