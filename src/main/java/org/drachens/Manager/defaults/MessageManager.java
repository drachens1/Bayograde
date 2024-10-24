package org.drachens.Manager.defaults;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.dataClasses.Provinces.Province;
import org.drachens.events.Factions.FactionCreateEvent;
import org.drachens.events.Factions.FactionDeleteEvent;
import org.drachens.events.Factions.FactionJoinEvent;
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
            Country creator = e.getCountry();
            Factions factions = e.getFaction();
            broadcast(Component.text()
                            .append(getPrefixes("faction"))
                            .append(creator.getNameComponent())
                            .append(Component.text(" has joined ",NamedTextColor.GREEN))
                            .append(factions.getNameComponent())
                            .build(),
                    creator.getCapital().getInstance());
        });
    }
}
