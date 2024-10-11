package org.drachens.cmd.country;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionsBasedOnInput;

public class Leader extends Command {
    public Leader() {
        super("leader");
        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            String[] a = context.getInput().split(" ");
            getSuggestionsBasedOnInput(suggestion, a[2], p.getInstance()).getEntries();
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            if (!getCountryNames(p.getInstance()).contains(context.get(countries))) {
                return;
            }
            Country c = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(c.getLeader().getName())
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .append(c.getLeader().getDescription())
                    .build());
        }, countries);
    }
}
