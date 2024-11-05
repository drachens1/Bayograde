package org.drachens.Manager.defaults;

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
    private final Component neutralComponent;
    private final Component gameOver;
    public MessageManager(){
        neutralComponent = Component.text()
                .append(Component.text("_________/", NamedTextColor.BLUE))
                .append(Component.text("Neutral", NamedTextColor.GOLD))
                .append(Component.text("\\_________\n", NamedTextColor.BLUE))
                .append(Component.text("Leader: \n"))
                .build();

        gameOver = Component.text()
                .append(getPrefixes("system"))
                .append(Component.text("Game Over",NamedTextColor.GREEN))
                .build();

        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();

        globEHandler.addListener(StartGameEvent.class, e->{
            e.getVotingOption().getMapGenerator().generate(e.getInstance(),e.getVotingOption());
            broadcast(Component.text()
                    .append(getPrefixes("system"))
                    .append(Component.text(e.getVotingOption().getName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                    .append(Component.text(" has won!!", NamedTextColor.GREEN))
                    .build(),e.getInstance());
        });

        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> {
            Province p = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(new Pos(e.getBlockPosition()));
            if (p==null)globalBroadcast("NULLl");
            if (!e.getPlayer().isSneaking()||playerHasCooldown(e.getPlayer())||!Objects.requireNonNull(p).isCapturable())return;
            cooldown(e.getPlayer());
            if (p.getOccupier() == null) {
                e.getPlayer().sendMessage(neutralComponent);
                return;
            }
            Country c = p.getOccupier();
            e.getPlayer().sendMessage(c.getDescription());
        });

        globEHandler.addListener(ResetEvent.class,e->broadcast(gameOver,e.getInstance()));

        globEHandler.addListener(StartWarEvent.class,e->{
            Country defender = e.getDefender();
            Country attacker = e.getAggressor();
            defender.addWar(attacker);
            attacker.addWar(defender);
            broadcast(Component.text()
                    .append(getPrefixes("country"))
                    .append(attacker.getNameComponent())
                    .append(Component.text(" started a war with ",NamedTextColor.RED))
                    .append(defender.getNameComponent())
                    .build(),
                    defender.getCapital().getInstance());
        });

        globEHandler.addListener(FactionCreateEvent.class,e->{
            Country creator = e.getCreator();
            Factions factions = e.getNewFaction();
            broadcast(Component.text()
                    .append(getPrefixes("faction"))
                    .append(creator.getNameComponent())
                    .append(Component.text(" has created ",NamedTextColor.GREEN))
                    .append(factions.getNameComponent())
                    .build(),
                    creator.getCapital().getInstance());
        });

        globEHandler.addListener(FactionDeleteEvent.class, e->{
            Country deleter = e.getDeleter();
            Factions factions = e.getDeletedFaction();
            broadcast(Component.text()
                            .append(getPrefixes("faction"))
                            .append(deleter.getNameComponent())
                            .append(Component.text(" has deleted ",NamedTextColor.GREEN))
                            .append(factions.getNameComponent())
                            .build(),
                    deleter.getCapital().getInstance());
        });

        globEHandler.addListener(FactionJoinEvent.class, e->{
            Country creator = e.country();
            broadcast(Component.text()
                            .append(getPrefixes("faction"))
                            .append(creator.getNameComponent())
                            .append(Component.text(" has joined ",NamedTextColor.GREEN))
                            .append(e.faction().getNameComponent())
                            .build(),
                    creator.getCapital().getInstance());
        });

        globEHandler.addListener(FactionInviteEvent.class, e->{
            Factions factions = e.getFaction();
            Country invited = e.getInvited();
            factions.sendMessage(Component.text()
                            .append(getPrefixes("faction"))
                            .append(invited.getNameComponent())
                            .append(Component.text(" has been invited to the faction",NamedTextColor.GREEN))
                    .build());
            invited.sendMessage(Component.text()
                            .append(getPrefixes("faction"))
                            .append(Component.text()
                                .append(Component.text(" you have been invited to join",NamedTextColor.GREEN))
                                .clickEvent(ClickEvent.runCommand("faction join "+factions.getStringName()))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to join the faction",NamedTextColor.GOLD)))
                            .build())
                            .append(factions.getName())

                    .build());
        });

        globEHandler.addListener(FactionKickEvent.class, e->{
            Factions factions = e.getFaction();
            Country country = e.getCountry();
            factions.sendMessage(Component.text()
                            .append(getPrefixes("faction"))
                            .append(country.getNameComponent())
                            .append(Component.text(" was kicked from the faction",NamedTextColor.RED))
                    .build());
            country.sendMessage(Component.text()
                            .append(getPrefixes("faction"))
                            .append(Component.text("You were kicked from "))
                    .append(factions.getName())
                    .build());
        });

        globEHandler.addListener(FactionSetLeaderEvent.class, e->{
            Factions factions = e.faction();
            Country country = e.country();
            factions.sendMessage(Component.text()
                    .append(getPrefixes("faction"))
                    .append(country.getNameComponent())
                    .append(Component.text(" has became the leader of the faction",NamedTextColor.GREEN))
                    .build());
            country.sendMessage(Component.text()
                    .append(getPrefixes("faction"))
                    .append(Component.text("assigned you as its leader "))
                    .append(factions.getName())
                    .build());
        });
    }
}
