package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class Leader extends Command {
    public Leader() {
        super("leader");
        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            getSuggestionBasedOnInput(suggestion, getCountryNames(p.getInstance()));
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null)
                return;
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(country.getLeader().getName())
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .append(country.getLeader().getDescription())
                    .build());
        }, countries);
    }
}
