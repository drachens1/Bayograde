package org.drachens.Manager.defaults;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerBlockInteractEvent;
import org.drachens.Manager.defaults.defaultsStorer.enums.InventoryEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Delay;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.dataClasses.territories.Province;
import org.drachens.events.Countries.CountryCoopPlayerEvent;
import org.drachens.events.Countries.CountrySetLeaderEvent;
import org.drachens.events.Factions.*;
import org.drachens.events.StartWarEvent;
import org.drachens.events.System.ResetEvent;
import org.drachens.events.System.StartGameEvent;
import org.drachens.events.demands.DemandAcceptedEvent;
import org.drachens.events.demands.DemandCompletionEvent;
import org.drachens.events.demands.DemandCounterOfferEvent;
import org.drachens.events.demands.DemandDeniedEvent;
import org.drachens.events.research.ResearchCompletionEvent;
import org.drachens.events.research.ResearchStartEvent;

import static org.drachens.util.KyoriUtil.getPrefixes;
import static org.drachens.util.Messages.broadcast;

public class MessageManager {
    private final Component system = getPrefixes("system");
    private final Component country = getPrefixes("country");
    private final Component factionPref = getPrefixes("faction");
    private final Component noOccupier = Component.text()
            .append(Component.text("_______/", NamedTextColor.BLUE))
            .append(Component.text("UNOCCUPIED", NamedTextColor.GOLD))
            .append(Component.text("\\_______", NamedTextColor.BLUE))
            .build();

    public MessageManager() {
        Component gameOver = Component.text()
                .append(system)
                .append(Component.text("Game Over", NamedTextColor.GREEN))
                .build();

        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();

        globEHandler.addListener(StartGameEvent.class, e -> {
            e.getVotingOption().getMapGenerator().generate(e.getInstance(), e.getVotingOption());
            InventoryEnum hotbarInventory = e.getVotingOption().getDefaultInventory();
            if (hotbarInventory!=null)
                e.getInstance().getPlayers().forEach(p -> ContinentalManagers.inventoryManager.assignInventory(p,hotbarInventory));
            broadcast(Component.text()
                    .append(system)
                    .append(Component.text(e.getVotingOption().getName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                    .append(Component.text(" has won!!", NamedTextColor.GREEN))
                    .build(), e.getInstance());
        });

        Delay provinceDelay = new Delay(100L);

        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> {
            Player player = e.getPlayer();
            if (!player.isSneaking())return;
            Province p = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(new Pos(e.getBlockPosition()));
            if (p == null || provinceDelay.hasCooldown(player)) return;
            provinceDelay.startCooldown(player);
            if (p.getOccupier()==null){
                player.sendMessage(noOccupier);
                return;
            }
            player.sendMessage(p.getDescription((CPlayer) player));
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
            Country creator = e.getCountry();
            broadcast(Component.text()
                            .append(factionPref)
                            .append(creator.getNameComponent())
                            .append(Component.text(" has joined ", NamedTextColor.GREEN))
                            .append(e.getCountry().getNameComponent())
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
            Factions factions = e.getFactions();
            Country country = e.getCountry();
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

        globEHandler.addListener(CountrySetLeaderEvent.class, e -> e.getCountry().sendMessage(Component.text()
                .append(country)
                .append(Component.text("The leader is now ", NamedTextColor.GREEN))
                .append(Component.text(e.getNewLeader().getUsername()))
                .build()));

        globEHandler.addListener(CountryCoopPlayerEvent.class, e -> {
            CPlayer cPlayer = e.getPlayer();
            Country country = e.getInviter();
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

        globEHandler.addListener(DemandCounterOfferEvent.class, e -> {
            Country from = e.getFrom();
            Country to = e.getTo();
            to.sendMessage(Component.text()
                    .append(country)
                    .append(from.getNameComponent())
                    .append(Component.text(" has started creating a counter offer", NamedTextColor.GREEN))
                    .build());
        });

        globEHandler.addListener(DemandAcceptedEvent.class, e -> {
            Country from = e.getFrom();
            Country to = e.getTo();
            Component description = e.getDemand().description();
            from.sendMessage(Component.text()
                    .append(country)
                    .append(to.getNameComponent())
                    .append(Component.text(" has accepted the demand\n"))
                    .append(description)
                    .build());
            to.sendMessage(Component.text()
                    .append(country)
                    .append(Component.text("Your country has accepted the demand\n"))
                    .append(description)
                    .build());
        });

        globEHandler.addListener(DemandDeniedEvent.class, e -> {
            Country from = e.getFrom();
            Country to = e.getTo();
            Component description = e.getDemand().description();
            from.sendMessage(Component.text()
                    .append(country)
                    .append(to.getNameComponent())
                    .append(Component.text(" has denied the demand\n"))
                    .append(description)
                    .build());
            to.sendMessage(Component.text()
                    .append(country)
                    .append(Component.text("Your country has denied the demand\n"))
                    .append(description)
                    .build());
        });

        globEHandler.addListener(DemandCompletionEvent.class, e -> {
            Country from = e.getFrom();
            Country to = e.getTo();
            to.sendMessage(Component.text()
                    .append(country)
                    .append(from.getNameComponent())
                    .append(Component.text(" has sent you a demand\n"))
                    .append(e.getDemand().description())
                    .append(Component.text()
                            .append(Component.text("[Accept]", NamedTextColor.GREEN, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to accept the demands", NamedTextColor.GREEN))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy demand accept")))
                    .append(Component.text()
                            .append(Component.text(" [Refuse]", NamedTextColor.RED, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to refuse the demands", NamedTextColor.RED))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy demand refuse")))
                    .appendNewline()
                    .append(Component.text("View: ", NamedTextColor.GREEN, TextDecoration.UNDERLINED, TextDecoration.BOLD))
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text(" [On]", NamedTextColor.GREEN, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to refuse the demands", NamedTextColor.GREEN))
                            .clickEvent(ClickEvent.runCommand("/demand incoming view off"))
                            .build())
                    .append(Component.text()
                            .append(Component.text(" [Off]", NamedTextColor.RED, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to refuse the demands", NamedTextColor.RED))
                            .clickEvent(ClickEvent.runCommand("/demand incoming view off"))
                            .build())
                    .build());
        });

        globEHandler.addListener(ResearchStartEvent.class,e-> e.getCountry().sendMessage(Component.text()
                        .append(country)
                        .append(Component.text("You have started researching "))
                        .append(Component.text(e.getResearchOption().getIdentifier()))
                .build()));

        globEHandler.addListener(ResearchCompletionEvent.class, e-> e.getCountry().sendMessage(Component.text()
                .append(country)
                .append(Component.text("You have finished researching "))
                .append(Component.text(e.getResearchOption().getIdentifier()))
                .build()));
    }
}
