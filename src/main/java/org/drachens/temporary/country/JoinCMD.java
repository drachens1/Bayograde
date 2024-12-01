package org.drachens.temporary.country;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class JoinCMD extends Command {
    public JoinCMD() {
        super("join");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Default usage: /country join <country>"));
        var countries = ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            getSuggestionBasedOnInput(suggestion, getCountryNames(p.getInstance()));
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
}

