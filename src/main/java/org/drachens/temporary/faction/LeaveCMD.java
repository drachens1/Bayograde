package org.drachens.temporary.faction;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.events.factions.FactionLeaveEvent;


public class LeaveCMD extends Command {
    public LeaveCMD() {
        super("leave");
        setCondition((sender, s) -> isInAFaction(sender));
        var factionsArg = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof CPlayer player) {
                        Country country = player.getCountry();
                        if (country != null && country.isInAFaction()) {
                            if (country.isInAnEconomicFaction()) {
                                suggestion.addEntry(new SuggestionEntry(country.getEconomyFactionType().getStringName()));
                            }
                            if (country.isInAMilitaryFaction()) {
                                suggestion.addEntry(new SuggestionEntry(country.getMilitaryFactionType().getStringName()));
                            }
                        }
                    }
                });

        addSyntax((sender, context) -> {
            if (!isInAFaction(sender)) return;
            CPlayer player = (CPlayer) sender;
            Country country = player.getCountry();
            Factions factions1 = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factionsArg));
            if (country == null || factions1 == null) return;
            if (!factions1.getMembers().contains(country)) return;
            factions1.removeCountry(country);
            EventDispatcher.call(new FactionLeaveEvent(factions1, country));

        }, factionsArg);
    }

    public boolean isInAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && country.isInAFaction();
        }
        return false;
    }
}
