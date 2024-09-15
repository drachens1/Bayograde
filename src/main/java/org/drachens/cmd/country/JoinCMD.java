package org.drachens.cmd.country;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionsBasedOnInput;
import static org.drachens.util.ServerUtil.getWorldClasses;

public class JoinCMD extends Command {
    public JoinCMD() {
        super("join");
        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            String[] a = context.getInput().split(" ");
            getSuggestionsBasedOnInput(suggestion, a[2], p.getInstance()).getEntries();
            System.out.println("After entry");
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            if (!getCountryNames(p.getInstance()).contains(context.get(countries))) {
                return;
            }
            Country country = getWorldClasses(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            country.changeCountry(p);
        }, countries);
    }

}

