package org.drachens.cmd.country;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getSuggestionsBasedOnInput;

public class JoinCMD extends Command {
    public JoinCMD() {
        super("join");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Default usage: /country join <country>"));
        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            getSuggestionsBasedOnInput(suggestion, context.getInput(), 2, p.getInstance()).getEntries();
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null)
                return;
            country.changeCountry(p);
        }, countries);
    }

    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof CPlayer p))
            return false;
        return p.getCountry() == null;
    }
}

