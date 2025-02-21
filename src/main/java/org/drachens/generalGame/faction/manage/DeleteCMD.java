package org.drachens.generalGame.faction.manage;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.player_types.CPlayer;

public class DeleteCMD extends Command {
    public DeleteCMD() {
        super("delete");
        setCondition((sender, s) -> leaderOfAFaction(sender));
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction delete <faction_name> "));

        var factions = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof CPlayer player) {
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

        addSyntax((sender, context) -> {
            CPlayer player = (CPlayer) sender;
            Country country = player.getCountry();
            Faction factionToDelete = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factions));
            if (factionToDelete != null && factionToDelete.isLeader(country))
                factionToDelete.delete();
        }, factions);
    }

    private boolean leaderOfAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && country.isLeaderOfAFaction();
        }
        return false;
    }
}
