package org.drachens.cmd.Dev.debug.CountryTypes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.defaultsStorer.enums.VotingWinner;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Countries.CountryEnums;
import org.drachens.temporary.clicks.ClicksCountry;
import org.drachens.temporary.troops.TroopCountry;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.ServerUtil.getWorldClasses;

public class CountryHistoryCMD extends Command {
    public CountryHistoryCMD(String permission) {
        super("history");
        setCondition((sender, permissionName) -> sender.hasPermission(permission));
        var type = ArgumentType.String("previousColonies | colony | colonialPower | upAndComing");
        type.setSuggestionCallback((sender, context, suggestion) -> {
            if (!sender.hasPermission(permission)) return;
            suggestion.addEntry(new SuggestionEntry("previousColonies"));
            suggestion.addEntry(new SuggestionEntry("colony"));
            suggestion.addEntry(new SuggestionEntry("colonialPower"));
            suggestion.addEntry(new SuggestionEntry("upAndComing"));
        });
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) return;
            if (!sender.hasPermission(permission)) return;
            List<Component> components = new ArrayList<>();
            CountryEnums.History choice = CountryEnums.History.valueOf(context.get(type));
            components.add(Component.text(context.get(type), NamedTextColor.BLUE));
            components.add(Component.newline());
            switch (ContinentalManagers.world(p.getInstance()).dataStorer().votingWinner){
                case VotingWinner.none -> {
                    return;
                }
                case VotingWinner.ww2_clicks -> {
                    for (Country c : getWorldClasses(p.getInstance()).countryDataManager().getCountries()) {
                        ClicksCountry country = (ClicksCountry) c;
                        if (country.getHistory().equals(choice)) {
                            components.add(Component.text(country.getName(), NamedTextColor.GOLD));
                            components.add(Component.text(", ", NamedTextColor.BLUE));
                        }
                    }
                }
                case VotingWinner.ww2_troops -> {
                    for (Country c : getWorldClasses(p.getInstance()).countryDataManager().getCountries()) {
                        TroopCountry country = (TroopCountry) c;
                        if (country.getHistory().equals(choice)) {
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
