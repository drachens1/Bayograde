package org.drachens.cmd.country;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.BetterCommand.IndividualCMD;

import static org.drachens.util.CommandsUtil.getSuggestionsBasedOnInput;
import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class JoinCMD extends IndividualCMD {
    public JoinCMD() {
        super("join");
        setDefaultExecutor((sender,context)-> sender.sendMessage("Default usage: /country join <country>"));
        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            String[] a = context.getInput().split(" ");
            getSuggestionsBasedOnInput(suggestion, a[2], p.getInstance()).getEntries();
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country==null)
                return;
            country.changeCountry(p);
        }, countries);
    }
    @Override
    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof Player p))
            return false;
        return getCountryFromPlayer(p)==null;
    }
}

