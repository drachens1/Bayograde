package org.drachens.generalGame.faction.manage;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.events.factions.FactionInviteEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class InviteCMD extends Command {
    public InviteCMD() {
        super("invite");
        setCondition((sender, s) -> leaderOfAFaction(sender));
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction invite <country_name> "));

        var factionsArg = ArgumentType.String("faction name")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof CPlayer player) {
                        Country country = player.getCountry();
                        if (country.isEconomyFactionLeader()) {
                            suggestion.addEntry(new SuggestionEntry(country.getEconomy().getEconomyFactionType().getStringName()));
                        }
                        if (country.isMilitaryFactionLeader()) {
                            suggestion.addEntry(new SuggestionEntry(country.getEconomy().getMilitaryFactionType().getStringName()));
                        }
                    }
                });

        var countryArg = getCountriesArgExcludingPlayersCountry();

        addSyntax((sender, context) -> {
            sender.sendMessage("Proper usage /faction manage invite <faction name> <country name>");

        }, factionsArg);

        addSyntax((sender, context) -> {
            CPlayer player = (CPlayer) sender;
            Country invited = ContinentalManagers.world(player.getInstance()).countryDataManager().getCountryFromName(context.get(countryArg));
            Faction faction1 = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factionsArg));
            if (faction1.getLeader() != player.getCountry()) return;
            EventDispatcher.call(new FactionInviteEvent(invited, faction1));
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
