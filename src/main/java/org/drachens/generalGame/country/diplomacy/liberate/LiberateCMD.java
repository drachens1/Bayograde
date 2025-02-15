package org.drachens.generalGame.country.diplomacy.liberate;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.events.countries.LiberationEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class LiberateCMD extends Command {
    public LiberateCMD() {
        super("liberate");
        var countries = ArgumentType.String("countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getOtherCountriesOccupier());
                });

        var type = ArgumentType.String("Type")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer)) {
                        return;
                    }
                    suggestion.addEntry(new SuggestionEntry("puppet"));
                    suggestion.addEntry(new SuggestionEntry("free"));
                });

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Proper usage /country diplomacy liberate <country>");
        });

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country target = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (target.hasCapitulated()) {
                p.sendMessage("You need to select a type if it has capitulated \n Proper usage /country diplomacy liberate <country> <type>");
                return;
            }
            if (country.getOthersCores(target).isEmpty()) {
                p.sendMessage("You don't occupy any of there cores");
                return;
            }
            EventDispatcher.call(new LiberationEvent(target, country, null));
        }, countries);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country target = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (target == null) {
                p.sendMessage("Target is null");
                return;
            }
            if (country.getOthersCores(target).isEmpty()) {
                p.sendMessage("You don't occupy any of there cores");
                return;
            }
            String t = context.get(type);
            if (!(t.equalsIgnoreCase("puppet") || t.equalsIgnoreCase("free"))) {
                p.sendMessage("Not a valid type");
                return;
            }
            EventDispatcher.call(new LiberationEvent(target, country, t));
        }, countries, type);
    }
}
