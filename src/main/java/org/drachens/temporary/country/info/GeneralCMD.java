package org.drachens.temporary.country.info;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class GeneralCMD extends Command {
    public GeneralCMD() {
        super("general");

        var countries = getCountriesArg();

        setDefaultExecutor((sender,context)->{
            if (!(sender instanceof CPlayer p)){
                return;
            }
            Country country = p.getCountry();
            if (country==null){
                p.sendMessage("Join a country or do /country info general <country>");
                return;
            }
            p.sendMessage(country.getDescription());
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null){
                p.sendMessage("That is not a valid country");
                return;
            }
            p.sendMessage(country.getDescription());
        }, countries);
    }
}
