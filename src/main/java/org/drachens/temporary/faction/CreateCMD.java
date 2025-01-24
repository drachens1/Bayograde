package org.drachens.temporary.faction;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.ConditionEnum;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.EconomyFactionType;
import org.drachens.dataClasses.Diplomacy.faction.MilitaryFactionType;
import org.drachens.events.factions.FactionCreateEvent;

public class CreateCMD extends Command {

    public CreateCMD() {
        super("create");

        setCondition((sender, s) -> notInAFaction(sender));

        var type = ArgumentType.Word("type")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof CPlayer player) {
                        Country country = player.getCountry();
                        if (country == null) return;
                        if (!country.isInAnEconomicFaction())
                            suggestion.addEntry(new SuggestionEntry("Economy"));
                        if (!country.isInAMilitaryFaction())
                            suggestion.addEntry(new SuggestionEntry("Military"));
                    }

                });

        var nameArg = ArgumentType.String("factionName");
        setDefaultExecutor((sender, context) -> {
            if (notInAFaction(sender)) {
                sender.sendMessage("Proper usage /faction create <type> <name> ");
            }
        });


        addConditionalSyntax((sender, s) -> notInAFaction(sender), (sender, context) -> {
            if (notInAFaction(sender))
                sender.sendMessage("Proper usage /faction create <type> <name> ");
        }, type);

        addConditionalSyntax((sender, s) -> notInAFaction(sender), (sender, context) -> {
            if (!notInAFaction(sender)) return;
            CPlayer player = (CPlayer) sender;
            Country country = player.getCountry();
            String factionName = context.get(nameArg);

            if (country.hasCondition(ConditionEnum.cant_join_faction)) {
                player.sendMessage(Component.text("You have the cant join a faction condition.", NamedTextColor.RED));
                return;
            }

            CountryDataManager countryDataManager = ContinentalManagers.world(player.getInstance()).countryDataManager();
            if (countryDataManager.getFactionNames().contains(factionName)) {
                player.sendMessage("A faction with that name already exists.");
                return;
            }

            String factionType = context.get(type);
            if ("Economy".equals(factionType)) {
                if (country.isInAnEconomicFaction()) {
                    sender.sendMessage("You are already in an economic faction");
                    return;
                }
                EconomyFactionType faction = new EconomyFactionType(country, factionName);
                country.setEconomyFactionType(faction);
                EventDispatcher.call(new FactionCreateEvent(country, faction));
            } else if ("Military".equals(factionType)) {
                if (country.isInAMilitaryFaction()) {
                    sender.sendMessage("You are already in an military faction");
                    return;
                }
                MilitaryFactionType faction = new MilitaryFactionType(country, factionName);
                country.setMilitaryFactionType(faction);
                EventDispatcher.call(new FactionCreateEvent(country, faction));
            } else {
                player.sendMessage("Enter a valid faction type.");
            }
        }, type, nameArg);
    }

    private boolean notInAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && country.isInAllFactions();
        }
        return false;
    }
}
