package org.drachens.temporary.faction.manage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

public class RenameCMD extends Command {
    public RenameCMD() {
        super("rename");
        setCondition((sender, s) -> leaderOfAFaction(sender));

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

        var newName = ArgumentType.String("name");

        Component notLeader = Component.text()
                .append(MessageEnum.faction.getComponent())
                .append(Component.text("You are not leader of this faction", NamedTextColor.RED))
                .build();

        Component nameTaken = Component.text()
                .append(MessageEnum.faction.getComponent())
                .append(Component.text("That name is taken"))
                .build();

        addSyntax((sender, context) -> {
            if (!leaderOfAFaction(sender)) return;
            CPlayer player = (CPlayer) sender;
            Faction faction = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factionsArg));
            Country country = player.getCountry();
            if (!faction.isLeader(country)) {
                player.sendMessage(notLeader);
                return;
            }
            String newNam = context.get(newName);
            if (ContinentalManagers.world(player.getInstance()).countryDataManager().factionExists(newNam)) {
                player.sendMessage(nameTaken);
                return;
            }
            faction.rename(newNam);
        }, factionsArg, newName);
    }

    private boolean leaderOfAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && country.isLeaderOfAFaction();
        }
        return false;
    }
}
