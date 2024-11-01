package org.drachens.cmd.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.events.Factions.FactionCreateEvent;
import org.drachens.events.Factions.FactionJoinEvent;

import java.util.Objects;

import static org.drachens.util.CommandsUtil.getFactionsSuggestionsBasedOnInput;
import static org.drachens.util.Messages.globalBroadcast;
import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class FactionCMD extends Command {
    public FactionCMD() {
        super("faction");

        joinCreation();
        createCreation();
        deleteCreation();

        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction <command>"));
    }

    private void joinCreation() {
        var join = ArgumentType.String("joinOption")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    sender.sendMessage("e");
                    if (notInAFaction(sender)) {
                        suggestion.addEntry(new SuggestionEntry("join"));
                    }
                });

        var factions = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    sender.sendMessage("hi");
                    if (notInAFaction(sender) && sender instanceof Player player) {
                        getFactionsSuggestionsBasedOnInput(suggestion, context.getInput(), player.getInstance());
                    }
                });

        addSyntax((sender, context) -> {
            if (!notInAFaction(sender)) return;
            Player player = (Player) sender;
            if (!Objects.equals(context.get(join), "join")) return;

            Factions faction = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factions));
            if (faction == null) {
                player.sendMessage("Cannot find that faction.");
                return;
            }

            Country country = getCountryFromPlayer(player);
            country.joinFaction(faction);
            EventDispatcher.call(new FactionJoinEvent(faction, country));
        }, join, factions);

        setArgumentCallback((sender, exception) -> sender.sendMessage("Invalid arguments! Usage: /faction join <faction_name>"), join);
    }

    private void createCreation() {
        var create = ArgumentType.String("createOption")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (notInAFaction(sender)) {
                        suggestion.addEntry(new SuggestionEntry("create"));
                    }
                });

        var type = ArgumentType.Word("type")
                .from("Economy", "Military")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    suggestion.addEntry(new SuggestionEntry("Economy"));
                    suggestion.addEntry(new SuggestionEntry("Military"));
                });

        var nameArg = ArgumentType.String("factionName");

        addSyntax((sender, context) -> {
            if (!notInAFaction(sender)) return;
            Player player = (Player) sender;
            Country country = getCountryFromPlayer(player);
            String factionName = context.get(nameArg);

            CountryDataManager countryDataManager = ContinentalManagers.world(player.getInstance()).countryDataManager();
            if (countryDataManager.getFactionNames().contains(factionName)) {
                player.sendMessage("A faction with that name already exists.");
                return;
            }

            String factionType = context.get(type);
            if ("Economy".equals(factionType)) {
                EconomyFactionType faction = new EconomyFactionType(country, factionName);
                country.setEconomyFactionType(faction);
                countryDataManager.addFaction(faction);
                EventDispatcher.call(new FactionCreateEvent(country, faction));
            } else if ("Military".equals(factionType)) {
                MilitaryFactionType faction = new MilitaryFactionType(country, factionName);
                country.setMilitaryFactionType(faction);
                countryDataManager.addFaction(faction);
                EventDispatcher.call(new FactionCreateEvent(country, faction));
            } else {
                player.sendMessage("Enter a valid faction type.");
            }
        }, create, type, nameArg);

        setArgumentCallback((sender, context) -> sender.sendMessage("Usage: /faction create <type> <faction_name>"), create);
    }

    private void deleteCreation() {
        System.out.println("Delete creation");
        var delete = ArgumentType.String("deleteOption")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    context.getInput();
                    globalBroadcast("RAN");
                    if (leaderOfAFaction(sender)) {
                        sender.sendMessage("added");
                        suggestion.addEntry(new SuggestionEntry("delete"));
                        return;
                    }
                    sender.sendMessage("Not added");
                });

        var factions = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (leaderOfAFaction(sender) && sender instanceof Player player) {
                        Country country = getCountryFromPlayer(player);
                        if (country != null) {
                            if (country.getEconomyFactionType() != null) {
                                suggestion.addEntry(new SuggestionEntry(country.getEconomyFactionType().getStringName()));
                            }
                            if (country.getMilitaryFactionType() != null) {
                                suggestion.addEntry(new SuggestionEntry(country.getMilitaryFactionType().getStringName()));
                            }
                        }
                    }
                });

        addSyntax((sender, context) -> {
            if (!leaderOfAFaction(sender)) return;
            Player player = (Player) sender;
            Country country = getCountryFromPlayer(player);
            Factions factionToDelete = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factions));
            if (factionToDelete != null && factionToDelete.getCreator() == country)
                factionToDelete.delete();
        }, delete, factions);

        setArgumentCallback((sender, context) -> sender.sendMessage("Usage: /faction delete <faction_name>"), delete);
    }

    private boolean leaderOfAFaction(CommandSender sender) {
        if (sender instanceof Player player) {
            Country country = getCountryFromPlayer(player);
            return country != null && country.isLeaderOfAFaction();
        }
        return false;
    }

    private boolean notInAFaction(CommandSender sender) {
        if (sender instanceof Player player) {
            Country country = getCountryFromPlayer(player);
            return country == null || !country.isInAllFactions();
        }
        return false;
    }
}
