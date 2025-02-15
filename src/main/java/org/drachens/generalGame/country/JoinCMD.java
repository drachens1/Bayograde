package org.drachens.generalGame.country;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class JoinCMD extends Command {
    public JoinCMD() {
        super("join");
        setCondition(((sender, commandString) -> !inCountry(sender)));
        setDefaultExecutor((sender, context) -> sender.sendMessage("Default usage: /country join <country>"));
        var countries = getCountriesArg();
        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null)
                return;
            country.addPlayer(p);
        }, countries);
    }

    private boolean inCountry(CommandSender sender){
        CPlayer p = (CPlayer) sender;
        return p.getCountry()!=null;
    }
}

