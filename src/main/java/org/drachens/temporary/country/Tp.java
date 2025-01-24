package org.drachens.temporary.country;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class Tp extends Command {
    public Tp() {
        super("tp");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Default usage: /country tp <country>"));
        var countries = getCountriesArg();

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            Country country = p.getCountry();
            if (country == null) {
                p.sendMessage("Join a country or do /country tp <country>");
                return;
            }
            p.teleport(country.getCapital().getPos().withY(1));
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null) {
                p.sendMessage("That is not a valid country");
                return;
            }
            p.teleport(country.getCapital().getPos().withY(1));
        }, countries);
    }
}
