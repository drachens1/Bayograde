package org.drachens.temporary.faction.manage;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.events.factions.FactionInviteEvent;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class InviteCMD extends Command {
    public InviteCMD() {
        super("invite");
        setCondition((sender, s) -> leaderOfAFaction(sender));
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction invite <country_name> "));

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

        var countryArg = getCountriesArgExcludingPlayersCountry();

        addSyntax((sender, context) -> {
        }, factionsArg);

        addSyntax((sender, context) -> {
            if (!leaderOfAFaction(sender)) return;
            CPlayer player = (CPlayer) sender;
            Country invited = ContinentalManagers.world(player.getInstance()).countryDataManager().getCountryFromName(context.get(countryArg));
            Factions factions1 = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factionsArg));
            if (factions1.getLeader() != player.getCountry()) return;
            EventDispatcher.call(new FactionInviteEvent(invited, factions1));
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
