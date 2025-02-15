package org.drachens.Manager.defaults;

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
import net.minestom.server.instance.Instance;
import org.drachens.Manager.YearManager;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.Manager.defaults.enums.InvitesEnum;
import org.drachens.Manager.defaults.enums.VotingWinner;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.bossbars.YearBar;
import org.drachens.cmd.ConfirmCMD;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Delay;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.dataClasses.Province;
import org.drachens.dataClasses.VotingOption;
import org.drachens.dataClasses.additional.GlobalGameWorldClass;
import org.drachens.dataClasses.customgame.CustomGameWorldClass;
import org.drachens.dataClasses.datastorage.DataStorer;
import org.drachens.dataClasses.datastorage.WorldClasses;
import org.drachens.dataClasses.other.ClientEntsToLoad;
import org.drachens.dataClasses.other.Clientside;
import org.drachens.events.CaptureBlockEvent;
import org.drachens.events.countries.CountryCoopPlayerEvent;
import org.drachens.events.countries.CountrySetLeaderEvent;
import org.drachens.events.countries.LiberationEvent;
import org.drachens.events.countries.demands.DemandAcceptedEvent;
import org.drachens.events.countries.demands.DemandCompletionEvent;
import org.drachens.events.countries.demands.DemandCounterOfferEvent;
import org.drachens.events.countries.demands.DemandDeniedEvent;
import org.drachens.events.countries.nonaggression.NonAggressionAcceptedEvent;
import org.drachens.events.countries.nonaggression.NonAggressionExpireEvent;
import org.drachens.events.countries.nonaggression.NonAggressionOfferEvent;
import org.drachens.events.countries.war.CapitulationEvent;
import org.drachens.events.countries.war.EndWarEvent;
import org.drachens.events.countries.war.StartWarEvent;
import org.drachens.events.countries.war.UnconditionalSurrenderEvent;
import org.drachens.events.countries.warjustification.WarJustificationCancelEvent;
import org.drachens.events.countries.warjustification.WarJustificationCompletionEvent;
import org.drachens.events.countries.warjustification.WarJustificationExpiresEvent;
import org.drachens.events.countries.warjustification.WarJustificationStartEvent;
import org.drachens.events.customgame.CustomGameInviteEvent;
import org.drachens.events.customgame.CustomGameKickEvent;
import org.drachens.events.factions.*;
import org.drachens.events.research.ResearchCompletionEvent;
import org.drachens.events.research.ResearchStartEvent;
import org.drachens.events.system.ResetEvent;
import org.drachens.events.system.StartGameEvent;
import org.drachens.generalGame.demand.WW2Demands;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.drachens.Manager.defaults.ContinentalManagers.inventoryManager;
import static org.drachens.Manager.defaults.ContinentalManagers.putWorldClass;
import static org.drachens.util.Messages.broadcast;

public class CentralEventManager {

    public CentralEventManager() {
        GlobalEventHandler globEHandler = MinecraftServer.getGlobalEventHandler();
        ConfirmCMD confirm = (ConfirmCMD) MinecraftServer.getCommandManager().getCommand("confirm");
        if (confirm==null)return;

        globEHandler.addListener(StartGameEvent.class, e -> {
            Instance instance = e.instance();
            VotingOption winner = e.votingOption();
            DataStorer dataStorer = ContinentalManagers.world(instance).dataStorer();
            dataStorer.votingWinner = VotingWinner.valueOf(winner.getName());
            dataStorer.votingOption = winner;

            e.votingOption().getMapGenerator().generate(e.instance(), e.votingOption());
            InventoryEnum hotbarInventory = e.votingOption().getDefaultInventory();
            if (hotbarInventory != null)
                e.instance().getPlayers().forEach(p -> ContinentalManagers.inventoryManager.assignInventory(p, hotbarInventory));

            YearManager yearManager = ContinentalManagers.yearManager;
            if (!yearManager.contains(instance)) {
                yearManager.addBar(instance);
            }
            YearBar yearBar = yearManager.getYearBar(instance);
            yearBar.run(e.votingOption());

            if (ContinentalManagers.world(instance).isGlobalGameWorldClass()){
                broadcast(Component.text().append(MessageEnum.system.getComponent())
                        .append(Component.text(e.votingOption().getName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                        .append(Component.text(" has won!!", NamedTextColor.GREEN)).build(), e.instance());
            }else if (ContinentalManagers.world(instance).isCustomGameWorldClass()) {
                broadcast(Component.text().append(MessageEnum.system.getComponent())
                        .append(Component.text(e.votingOption().getName(), NamedTextColor.GREEN, TextDecoration.BOLD))
                        .append(Component.text(" has started", NamedTextColor.GREEN)).build(), e.instance());
            }

        });

        Delay provinceDelay = new Delay(100L);

        globEHandler.addListener(PlayerBlockInteractEvent.class, e -> {
            Player player = e.getPlayer();
            if (!player.isSneaking()) return;
            Province p = ContinentalManagers.world(e.getInstance()).provinceManager().getProvince(new Pos(e.getBlockPosition()));
            if (p == null || provinceDelay.hasCooldown(player)) return;
            provinceDelay.startCooldown(player);
            if (p.getOccupier() == null) {
                return;
            }
            player.sendMessage(p.getDescription((CPlayer) player));
        });

        globEHandler.addListener(ResetEvent.class, e -> {
            Instance instance = e.instance();
            WorldClasses worldClasses = ContinentalManagers.world(instance);
            if (worldClasses.isGlobalGameWorldClass()){
                GlobalGameWorldClass globalGameWorldClass = worldClasses.getAsGlobalGameWorldClass();
                YearBar yearBar = ContinentalManagers.yearManager.getYearBar(instance);
                yearBar.cancelTask();
                globalGameWorldClass.votingManager().reset();
                CountryDataManager countryDataManager = globalGameWorldClass.countryDataManager();
                countryDataManager.getCountries().forEach((Country::endGame));
                ClientEntsToLoad clientEntsToLoad = globalGameWorldClass.clientEntsToLoad();
                if (clientEntsToLoad.getClientSides(e.instance()) != null) {
                    new ArrayList<>(clientEntsToLoad.getClientSides(e.instance())).forEach((Clientside::dispose));
                }
                e.instance().getPlayers().forEach(player -> {
                    CPlayer p = (CPlayer) player;
                    if (p.getCountry() != null) {
                        p.getCountry().removePlayer(p);
                    }
                });
                clientEntsToLoad.reset();
                CountryDataManager c = new CountryDataManager(instance, new ArrayList<>());
                putWorldClass(instance, new GlobalGameWorldClass(
                        c,
                        clientEntsToLoad,
                        globalGameWorldClass.votingManager(),
                        globalGameWorldClass.provinceManager(),
                        new DataStorer()
                ));
                broadcast(Component.text().append(MessageEnum.system.getComponent()).append(Component.text("Game Over", NamedTextColor.GREEN)).build(), e.instance());

            }
            if (worldClasses.isCustomGameWorldClass()){
                CustomGameWorldClass customGameWorldClass = worldClasses.getAsCustomGameWorldClass();
                YearBar yearBar = ContinentalManagers.yearManager.getYearBar(instance);
                yearBar.cancelTask();
                CountryDataManager countryDataManager = customGameWorldClass.countryDataManager();
                countryDataManager.getCountries().forEach((Country::endGame));
                ClientEntsToLoad clientEntsToLoad = customGameWorldClass.clientEntsToLoad();
                if (clientEntsToLoad.getClientSides(e.instance()) != null) {
                    new ArrayList<>(clientEntsToLoad.getClientSides(e.instance())).forEach((Clientside::dispose));
                }
                e.instance().getPlayers().forEach(player -> {
                    CPlayer p = (CPlayer) player;
                    if (p.getCountry() != null) {
                        p.getCountry().removePlayer(p);
                    }
                });
                clientEntsToLoad.reset();
                broadcast(Component.text().append(MessageEnum.system.getComponent()).append(Component.text("Game Over", NamedTextColor.GREEN)).build(), e.instance());
                ContinentalManagers.removeWorldClass(instance);
            }
        });

        globEHandler.addListener(CaptureBlockEvent.class, e -> {
            Country occupier = e.defender();
            Country attacker = e.attacker();
            occupier.sendActionBar(Component.text("You have been attacked at " + e.attacked().getPos(), NamedTextColor.RED));
            e.attacked().setOccupier(attacker);
        });

        globEHandler.addListener(StartWarEvent.class, e -> {
            Country defender = e.defender();
            Country attacker = e.attacker();
            attacker.addModifier(e.warJustification().getModifier());
            attacker.removeCompletedWarJustification(defender.getName());
            List<Component> warsWith = new ArrayList<>();
            List<Country> atks = new ArrayList<>();
            atks.add(attacker);
            if (attacker.hasPuppets()) {
                atks.addAll(attacker.getPuppets());
                warsWith.removeLast();
            } else if (attacker.hasOverlord()) {
                atks.addAll(attacker.getOverlord().getPuppets());
                atks.add(attacker.getOverlord());
            }

            List<Component> warsAgainst = new ArrayList<>();
            List<Country> defs = new ArrayList<>();
            defs.add(defender);
            if (defender.hasPuppets()) {
                defs.addAll(defender.getPuppets());
            } else if (defender.hasOverlord()) {
                defs.addAll(defender.getOverlord().getPuppets());
                defs.add(defender.getOverlord());
            } else {
                defender.addCountryWar(attacker);
            }

            defs.forEach(country -> atks.forEach(country1 -> {
                country.addCountryWar(country1);
                country1.addCountryWar(country);
            }));

            atks.forEach(country -> {
                warsWith.add(country.getNameComponent());
                warsWith.add(Component.text(", "));
            });
            warsWith.removeLast();

            defs.forEach(country -> {
                warsAgainst.add(country.getNameComponent());
                warsAgainst.add(Component.text(", "));
            });
            warsAgainst.removeLast();

            broadcast(Component.text()
                    .append(MessageEnum.system.getComponent())
                    .append(warsWith)
                    .append(Component.text(" started a war with ", NamedTextColor.RED))
                    .append(warsAgainst)
                    .build(), e.attacker().getInstance());
        });

        globEHandler.addListener(EndWarEvent.class, e -> {
            Country defender = e.from();
            Country attacker = e.to();
            attacker.removeWar(defender);
            defender.removeWar(attacker);
        });

        globEHandler.addListener(CapitulationEvent.class, e -> {
            Country defender = e.from();
            Country attacker = e.to();
            attacker.removeWar(defender);
            defender.removeWar(attacker);
            if (attacker.hasOverlord()) {
                attacker = attacker.getOverlord();
            }
            if (attacker.hasPuppets()) {
                Country finalAttacker1 = attacker;
                attacker.getPuppets().forEach(country -> {
                    if (country.occupiesCoresFrom(defender)) {
                        new ArrayList<>(country.getOthersCores(defender)).forEach(province -> province.liberate(finalAttacker1));
                    }
                });
            }
            defender.capitulate(attacker);
            broadcast(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(defender.getNameComponent())
                    .append(Component.text(" has capitulated to ", NamedTextColor.RED))
                    .append(attacker.getNameComponent())
                    .build(), defender.getInstance());

            attacker.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("Capitulation event options: "))
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text("[ANNEX]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to annex " + defender.getName(), NamedTextColor.GRAY)))
                    )
                    .append(Component.text()
                            .append(Component.text(" [LIBERATE] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to liberate all there occupied cores", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy liberate " + defender.getName() + " free "))
                    )
                    .append(Component.text()
                            .append(Component.text("[PUPPET]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to release all of there occupied cores as a puppet", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy liberate " + defender.getName() + " puppet "))
                    )
                    .build());
        });

        globEHandler.addListener(UnconditionalSurrenderEvent.class, e -> {
            Country defender = e.from();
            Country attacker = e.to();
            attacker.removeWar(defender);
            defender.removeWar(attacker);
            defender.capitulate(attacker);
            broadcast(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text(defender.getPlayerLeader().getUsername()))
                    .append(Component.text(" has decided to unconditionally surrender to ", NamedTextColor.RED))
                    .append(attacker.getNameComponent())
                    .build(), defender.getInstance());
        });

        globEHandler.addListener(FactionCreateEvent.class, e -> {
            if (!ContinentalManagers.world(e.creator().getInstance()).dataStorer().votingOption.isFactionsEnabled())return;
            Country creator = e.creator();
            Faction faction = e.newFaction();
            broadcast(Component.text()
                            .append(MessageEnum.faction.getComponent())
                            .append(creator.getNameComponent())
                            .append(Component.text(" has created ", NamedTextColor.GREEN))
                            .append(faction.getNameComponent())
                            .build(),
                    creator.getCapital().getInstance());
        });

        globEHandler.addListener(FactionDeleteEvent.class, e -> {
            Country deleter = e.deleter();
            Faction faction = e.deletedFaction();
            broadcast(Component.text()
                            .append(MessageEnum.faction.getComponent())
                            .append(deleter.getNameComponent())
                            .append(Component.text(" has deleted ", NamedTextColor.GREEN))
                            .append(faction.getNameComponent())
                            .build(),
                    deleter.getCapital().getInstance());
        });

        globEHandler.addListener(FactionJoinEvent.class, e -> {
            Faction faction = e.faction();
            Country country = e.country();
            if (faction instanceof MilitaryFactionType militaryFactionType) {
                if (!country.canJoinFaction(militaryFactionType)) return;
                militaryFactionType.getMembers().forEach(country1 -> country1.getOccupies().forEach(province -> country1.getAllyWorld().removeGhostBlock(province.getPos())));
                militaryFactionType.addMember(country);
                country.setMilitaryFactionType(militaryFactionType);
                country.createInfo();

            } else if (faction instanceof EconomyFactionType economyFactionType) {
                if (!country.canJoinFaction(economyFactionType)) return;
                country.setEconomyFactionType(economyFactionType);
                economyFactionType.addCountry(country);
                country.createInfo();
            }
            broadcast(Component.text()
                            .append(MessageEnum.faction.getComponent())
                            .append(country.getNameComponent())
                            .append(Component.text(" has joined ", NamedTextColor.GREEN))
                            .append(faction.getNameComponent())
                            .build(),
                    country.getCapital().getInstance());
        });

        globEHandler.addListener(FactionInviteEvent.class, e -> {
            Faction faction = e.faction();
            faction.addToInvites(e.invited());
            e.invited().addInvite(InvitesEnum.faction, faction.getStringName(), faction);
            Country invited = e.invited();
            faction.sendMessage(Component.text()
                    .append(MessageEnum.faction.getComponent())
                    .append(invited.getNameComponent())
                    .append(Component.text(" has been invited to the faction", NamedTextColor.GREEN))
                    .build());
            invited.sendMessage(Component.text()
                    .append(MessageEnum.faction.getComponent())
                    .append(Component.text(" you have been invited to join ", NamedTextColor.GREEN))
                    .append(faction.getNameComponent())
                    .append(Component.text()
                            .append(Component.text(" [CLICK]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to join the faction", NamedTextColor.GRAY))
                            .clickEvent(ClickEvent.runCommand("/faction join " + faction.getStringName())))
                    .build());
        });

        globEHandler.addListener(FactionKickEvent.class, e -> {
            Faction faction = e.faction();
            faction.removeCountry(e.country());
            Country country = e.country();
            faction.sendMessage(Component.text()
                    .append(MessageEnum.faction.getComponent())
                    .append(country.getNameComponent())
                    .append(Component.text(" was kicked from the faction", NamedTextColor.RED))
                    .build());
            country.sendMessage(Component.text()
                    .append(MessageEnum.faction.getComponent())
                    .append(Component.text("You were kicked from "))
                    .append(faction.getName())
                    .build());
        });

        globEHandler.addListener(FactionSetLeaderEvent.class, e -> {
            Faction faction = e.faction();
            Country country = e.country();
            faction.setLeader(country);
            faction.sendMessage(Component.text()
                    .append(MessageEnum.faction.getComponent())
                    .append(country.getNameComponent())
                    .append(Component.text(" has became the leader of the faction", NamedTextColor.GREEN))
                    .build());
            country.sendMessage(Component.text()
                    .append(MessageEnum.faction.getComponent())
                    .append(Component.text("assigned you as its leader "))
                    .append(faction.getName())
                    .build());
        });

        globEHandler.addListener(CountrySetLeaderEvent.class, e -> {
            e.country().setPlayerLeader(e.newLeader());
            e.country().sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("The leader is now ", NamedTextColor.GREEN))
                    .append(Component.text(e.newLeader().getUsername()))
                    .build());
        });

        globEHandler.addListener(CountryCoopPlayerEvent.class, e -> {
            CPlayer cPlayer = e.p();
            Country country = e.inviter();
            country.addInvite(InvitesEnum.player,cPlayer.getUsername(),cPlayer);
            cPlayer.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("You have been invited to join ", NamedTextColor.GREEN))
                    .append(country.getNameComponent())
                            .append(Component.text()
                                    .append(Component.text("[ACCEPT]",NamedTextColor.GOLD,TextDecoration.BOLD))
                                    .hoverEvent(Component.text("Click to join", NamedTextColor.GREEN))
                                    .clickEvent(ClickEvent.runCommand("/country accept " + country.getName())))
                    .build());
            country.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text(cPlayer.getUsername()))
                    .append(Component.text(" has been invited to join the faction", NamedTextColor.GREEN))
                    .build());
        });

        globEHandler.addListener(DemandCounterOfferEvent.class, e -> {
            Country from = e.from();
            Country to = e.to();
            Demand demand = new WW2Demands(to, from);
            demand.copyButOpposite(e.original());
            ContinentalManagers.demandManager.addActive(to, demand);
            inventoryManager.assignInventory(from.getPlayerLeader(), InventoryEnum.demand);
            to.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(from.getNameComponent())
                    .append(Component.text(" has started creating a counter offer", NamedTextColor.GREEN))
                    .build());
        });

        globEHandler.addListener(DemandAcceptedEvent.class, e -> {
            Country from = e.from();
            Country to = e.to();
            e.demand().accepted();
            Component description = e.demand().description();
            from.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(to.getNameComponent())
                    .append(Component.text(" has accepted the demand\n"))
                    .append(description)
                    .build());
            to.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("Your country has accepted the demand\n"))
                    .append(description)
                    .build());
        });

        globEHandler.addListener(DemandDeniedEvent.class, e -> {
            Country from = e.from();
            Country to = e.to();
            e.demand().accepted();
            Component description = e.demand().description();
            from.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(to.getNameComponent())
                    .append(Component.text(" has denied the demand\n"))
                    .append(description)
                    .build());
            to.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("Your country has denied the demand\n"))
                    .append(description)
                    .build());
        });

        globEHandler.addListener(DemandCompletionEvent.class, e -> {
            Country from = e.from();
            Country to = e.to();
            e.demand().complete();
            to.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(from.getNameComponent())
                    .append(Component.text(" has sent you a demand\n"))
                    .append(e.demand().description())
                    .append(Component.text()
                            .append(Component.text("[Accept]", NamedTextColor.GREEN, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to accept the demands", NamedTextColor.GREEN))
                            .clickEvent(ClickEvent.runCommand("/demand incoming " + from.getName() + " accept")))
                    .append(Component.text()
                            .append(Component.text(" [Refuse]", NamedTextColor.RED, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to refuse the demands", NamedTextColor.RED))
                            .clickEvent(ClickEvent.runCommand("/demand incoming " + from.getName() + " refuse")))
                    .appendNewline()
                    .append(Component.text("View: ", NamedTextColor.GREEN, TextDecoration.UNDERLINED, TextDecoration.BOLD))
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text(" [On]", NamedTextColor.GREEN, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to refuse the demands", NamedTextColor.GREEN))
                            .clickEvent(ClickEvent.runCommand("/demand incoming " + from.getName() + " view on"))
                            .build())
                    .append(Component.text()
                            .append(Component.text(" [Off]", NamedTextColor.RED, TextDecoration.BOLD))
                            .hoverEvent(Component.text("Click to refuse the demands", NamedTextColor.RED))
                            .clickEvent(ClickEvent.runCommand("/demand incoming " + from.getName() + " view off"))
                            .build())
                    .build());
        });

        globEHandler.addListener(ResearchStartEvent.class, e -> {
            e.country().getResearchCountry().setCurrentResearch(e.researchOption());
            e.country().sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("You have started researching ", NamedTextColor.GREEN))
                    .append(e.researchOption().getName())
                    .build());
        });

        globEHandler.addListener(ResearchCompletionEvent.class, e -> {
            e.country().getResearchCountry().completeActiveResearch();
            e.country().sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("You have finished researching ", NamedTextColor.GREEN))
                    .append(e.researchOption().getName())
                    .build());
        });

        globEHandler.addListener(WarJustificationStartEvent.class, e -> {
            Country against = e.warJustification().getAgainstCountry();
            Country from = e.from();
            from.addWarJustification(e.warJustification());
            against.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(from.getNameComponent())
                    .append(Component.text(" has started justifying against you", NamedTextColor.GREEN))
                    .build());
            from.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("You have started justifying against ", NamedTextColor.GREEN))
                    .append(against.getNameComponent())
                    .build());
        });

        globEHandler.addListener(WarJustificationCancelEvent.class, e -> {
            Country against = e.warJustification().getAgainstCountry();
            Country from = e.from();
            from.removeWarJustification(against.getName());
            against.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(from.getNameComponent())
                    .append(Component.text(" has cancelled the justification against you", NamedTextColor.GREEN))
                    .build());
            from.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("You have cancelled the justification against ", NamedTextColor.GREEN))
                    .append(against.getNameComponent())
                    .build());
        });

        globEHandler.addListener(WarJustificationCompletionEvent.class, e -> { //Not cancelable
            Country against = e.warJustification().getAgainstCountry();
            Country from = e.from();
            against.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(from.getNameComponent())
                    .append(Component.text(" has completed the justification against you", NamedTextColor.GREEN))
                    .build());
            from.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("Your justification against ", NamedTextColor.GREEN))
                    .append(against.getNameComponent())
                    .append(Component.text(" has finished", NamedTextColor.GREEN))
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text("[DECLARE WAR]", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to declare war on " + against.getName(), NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy declare-war " + against.getName()))
                    )
                    .build());
        });

        globEHandler.addListener(WarJustificationExpiresEvent.class, e -> { //Not cancelable
            Country against = e.warJustification().getAgainstCountry();
            Country from = e.from();
            against.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(from.getNameComponent())
                    .append(Component.text(" has let the justification expire", NamedTextColor.GREEN))
                    .build());
            from.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("Your justification against ", NamedTextColor.GREEN))
                    .append(against.getNameComponent())
                    .append(Component.text(" has expired", NamedTextColor.GREEN))
                    .build());
        });

        globEHandler.addListener(LiberationEvent.class, e -> {
            Country target = e.liberated();
            Country country = e.liberator();

            new ArrayList<>(country.getOthersCores(target)).forEach(province -> province.liberate(target));
            if (target.hasCapitulated()) {
                target.setCapitulated(false);
                target.calculateCapitulationPercentage();
                broadcast(Component.text()
                        .append(MessageEnum.country.getComponent())
                        .append(target.getNameComponent())
                        .append(Component.text(" has been liberated by "))
                        .append(country.getNameComponent())
                        .build(), country.getInstance());
                if (Objects.equals(e.type(), "puppet")) {
                    target.puppet(country);
                    country.addPuppet(target);
                }
                return;
            }
            target.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(country.getNameComponent())
                    .append(Component.text(" has returned occupied land to you"))
                    .build());

        });

        globEHandler.addListener(NonAggressionOfferEvent.class, e->{
            NonAggressionPact nonAggressionPact = e.nonAggressionPact();
            Country from = nonAggressionPact.getFrom();
            Country to = nonAggressionPact.getTo();
            to.addInvite(InvitesEnum.nonaggression,from.getName(),nonAggressionPact);
            to.sendMessage(Component.text()
                            .append(MessageEnum.country.getComponent())
                            .append(Component.text("You have received a non-aggression pact from ",NamedTextColor.GREEN))
                            .append(from.getNameComponent())
                            .appendNewline()
                            .append(Component.text("Non-aggression pact duration: "+nonAggressionPact.getDuration()+" days"))
                            .append(Component.text()
                                    .append(Component.text(" [ACCEPT]",NamedTextColor.GOLD,TextDecoration.BOLD))
                                    .hoverEvent(HoverEvent.showText(Component.text("Click to accept the non-aggression pact", NamedTextColor.GRAY)))
                                    .clickEvent(ClickEvent.runCommand("/country diplomacy non-aggression accept " + from.getName())))
                    .build());
            from.sendMessage(Component.text()
                            .append(MessageEnum.country.getComponent())
                            .append(Component.text("You have sent a non-aggression pact offer to ",NamedTextColor.GREEN))
                            .append(to.getNameComponent())
                    .build());
        });

        globEHandler.addListener(NonAggressionExpireEvent.class, e->{
            NonAggressionPact nonAggressionPact = e.nonAggressionPact();
            Country from = nonAggressionPact.getFrom();
            Country to = nonAggressionPact.getTo();
            from.removeNonAggressionPact(nonAggressionPact,to);
            to.removeNonAggressionPact(nonAggressionPact,from);
            from.loadCountriesDiplomacy(to);
            to.loadCountriesDiplomacy(from);
            to.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("The non-aggression pact from ",NamedTextColor.GREEN))
                    .append(from.getNameComponent())
                    .append(Component.text(" has expired",NamedTextColor.GREEN))
                    .build());
            from.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("The non-aggression pact from ",NamedTextColor.GREEN))
                    .append(to.getNameComponent())
                    .append(Component.text(" has expired",NamedTextColor.GREEN))
                    .build());
        });

        globEHandler.addListener(NonAggressionAcceptedEvent.class, e->{
            NonAggressionPact nonAggressionPact = e.nonAggressionPact();
            Country from = nonAggressionPact.getFrom();
            Country to = nonAggressionPact.getTo();
            to.addInvite(InvitesEnum.nonaggression,from.getName(),nonAggressionPact);
            to.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("You have accepted the non-aggression pact from ",NamedTextColor.GREEN))
                    .append(from.getNameComponent())
                    .build());
            from.sendMessage(Component.text()
                    .append(MessageEnum.country.getComponent())
                    .append(Component.text("The non-aggression pact sent to ",NamedTextColor.GREEN))
                    .append(to.getNameComponent())
                    .append(Component.text(" has been accepted",NamedTextColor.GREEN))
                    .build());
            to.addNonAggressionPact(nonAggressionPact,from);
            from.addNonAggressionPact(nonAggressionPact,to);
        });

        globEHandler.addListener(CustomGameInviteEvent.class, e->{
            CPlayer inviter = e.inviter();
            CPlayer invited = e.invited();
            broadcast(Component.text()
                    .append(MessageEnum.system.getComponent())
                    .append(Component.text(inviter.getUsername()))
                    .append(Component.text(" has invited ",NamedTextColor.GREEN))
                    .append(Component.text(invited.getUsername()))
                    .append(Component.text(" to play",NamedTextColor.GREEN))
                    .build(),inviter.getInstance());

            confirm.putPlayerRunnable(invited, () -> invited.setInstance(inviter.getInstance()));

            invited.sendMessage(Component.text()
                            .append(MessageEnum.system.getComponent())
                            .append(Component.text("You have been invited to play a custom game with ",NamedTextColor.GREEN))
                            .append(Component.text(inviter.getUsername()))
                            .append(Component.text()
                                    .append(Component.text(" [ACCEPT] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to join", NamedTextColor.GRAY)))
                                    .clickEvent(ClickEvent.runCommand("/confirm")))
                    .build());
        });

        globEHandler.addListener(CustomGameKickEvent.class, e->{
            Player p = e.kicked();
            if (p.getInstance() != ContinentalManagers.worldManager.getDefaultWorld().getInstance()) {
                p.setInstance(ContinentalManagers.worldManager.getDefaultWorld().getInstance());
            }
            p.teleport(new Pos(0, 1, 0));
        });
    }
}
