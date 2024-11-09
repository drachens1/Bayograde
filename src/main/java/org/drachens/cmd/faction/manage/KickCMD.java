package org.drachens.cmd.faction.manage;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.events.Factions.FactionKickEvent;

import static org.drachens.util.CommandsUtil.*;


public class KickCMD extends Command {
    public KickCMD() {
        super("kick");
        setCondition((sender,s)->leaderOfAFaction(sender));

        var factionsArg = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (leaderOfAFaction(sender) && sender instanceof CPlayer player) {
                        Country country = player.getCountry();
                        if (country != null) {
                            if (country.getEconomyFactionType() != null && country.getEconomyFactionType().isLeader(country)) {
                                suggestion.addEntry(new SuggestionEntry(country.getEconomyFactionType().getStringName()));
                            }
                            if (country.getMilitaryFactionType() != null && country.getEconomyFactionType().isLeader(country)) {
                                suggestion.addEntry(new SuggestionEntry(country.getMilitaryFactionType().getStringName()));
                            }
                        }
                    }
                });

        var countryArg = ArgumentType.String("countryName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (leaderOfAFaction(sender) && sender instanceof CPlayer player) {
                        getSuggestionBasedOnInput(suggestion, context.getInput(), 2, getCountryNames(player.getInstance()));
                    }
                });

        addSyntax((sender, context) -> {
            if (!leaderOfAFaction(sender)) return;
            CPlayer player = (CPlayer) sender;
            Country country = ContinentalManagers.world(player.getInstance()).countryDataManager().getCountryFromName(context.get(countryArg));
            Factions factions1 = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factionsArg));
            if (country==null || factions1==null)return;
            if (!factions1.getMembers().contains(country))return;
            factions1.removeCountry(country);
            EventDispatcher.call(new FactionKickEvent(factions1,country));

        }, factionsArg, countryArg);
    }

    private boolean leaderOfAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && country.isLeaderOfAFaction();
        }
        return false;
    }
}
