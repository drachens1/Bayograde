package org.drachens.cmd.Dev.debug.CountryTypes;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.VotingWinner;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.CountryEnums;
import org.drachens.temporary.clicks.ClicksCountry;
import org.drachens.temporary.troops.TroopCountry;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.ServerUtil.getWorldClasses;

public class CountryTypesCMD extends Command {
    public CountryTypesCMD(String permission) {
        super("types");
        setCondition((sender, permissionName) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission(permission);
        });
        var type = ArgumentType.String("superpower | major | minor");
        type.setSuggestionCallback((sender, context, suggestion) -> {
            CPlayer p = (CPlayer) sender;
            if (!p.hasPermission(permission)) return;
            suggestion.addEntry(new SuggestionEntry("superPower"));
            suggestion.addEntry(new SuggestionEntry("major"));
            suggestion.addEntry(new SuggestionEntry("minor"));
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            if (!p.hasPermission(permission)) return;
            List<Component> components = new ArrayList<>();
            CountryEnums.Type choice = CountryEnums.Type.valueOf(context.get(type));
            components.add(Component.text(context.get(type), NamedTextColor.BLUE));
            components.add(Component.newline());
            switch (ContinentalManagers.world(p.getInstance()).dataStorer().votingWinner) {
                case VotingWinner.none -> {
                    return;
                }
                case VotingWinner.ww2_clicks -> {
                    for (Country c : getWorldClasses(p.getInstance()).countryDataManager().getCountries()) {
                        ClicksCountry country = (ClicksCountry) c;
                        if (country.getType().equals(choice)) {
                            components.add(Component.text(country.getName(), NamedTextColor.GOLD));
                            components.add(Component.text(", ", NamedTextColor.BLUE));
                        }
                    }
                }
                case VotingWinner.ww2_troops -> {
                    for (Country c : getWorldClasses(p.getInstance()).countryDataManager().getCountries()) {
                        TroopCountry country = (TroopCountry) c;
                        if (country.getType().equals(choice)) {
                            components.add(Component.text(country.getName(), NamedTextColor.GOLD));
                            components.add(Component.text(", ", NamedTextColor.BLUE));
                        }
                    }
                }
            }

            p.sendMessage(Component.text()
                    .append(components)
                    .build());
        }, type);
    }
}
