package org.drachens.cmd.country;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.*;

public class Members extends Command {
    public Members() {
        super("members");
        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            String[] a = context.getInput().split(" ");
            getSuggestionBasedOnInput(suggestion, context.getInput(), 2, getCountryNames(p.getInstance()));
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country==null)
                return;
        }, countries);
    }

    public boolean requirements(CommandSender sender) {
        return sender instanceof Player;
    }
}
