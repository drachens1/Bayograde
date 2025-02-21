package org.drachens.generalGame.faction.manage;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.events.factions.FactionKickEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;


public class KickCMD extends Command {
    public KickCMD() {
        super("kick");
        setCondition((sender, s) -> leaderOfAFaction(sender));

        Argument<String> factionsArg = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof CPlayer player) {
                        Country country = player.getCountry();
                        if (null != country) {
                            if (country.isEconomyFactionLeader()) {
                                suggestion.addEntry(new SuggestionEntry(country.getEconomy().getEconomyFactionType().getStringName()));
                            }
                            if (country.isMilitaryFactionLeader()) {
                                suggestion.addEntry(new SuggestionEntry(country.getEconomy().getMilitaryFactionType().getStringName()));
                            }
                        }
                    }
                });

        Argument<String> countryArg = ArgumentType.String("countryName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof CPlayer player) {
                        getSuggestionBasedOnInput(suggestion, getCountryNames(player.getInstance()));
                    }
                });

        addSyntax((sender, context) -> {
            CPlayer player = (CPlayer) sender;
            Country country = ContinentalManagers.world(player.getInstance()).countryDataManager().getCountryFromName(context.get(countryArg));
            Faction faction1 = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factionsArg));
            if (null == country || null == faction1) return;
            if (!faction1.getMembers().contains(country)) return;
            EventDispatcher.call(new FactionKickEvent(faction1, country));

        }, factionsArg, countryArg);
    }

    private boolean leaderOfAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return null != country && country.isLeaderOfAFaction();
        }
        return false;
    }
}
