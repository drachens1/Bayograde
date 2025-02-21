package org.drachens.generalGame.country;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class Tp extends Command {
    public Tp() {
        super("tp");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Default usage: /country tp <country>"));
        Argument<String> countries = getCountriesArg();

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            Country country = p.getCountry();
            if (null == country) {
                p.sendMessage("Join a country or do /country tp <country>");
                return;
            }
            p.teleport(country.getInfo().getCapital().getPos().withY(1));
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == country) {
                p.sendMessage("That is not a valid country");
                return;
            }
            p.teleport(country.getInfo().getCapital().getPos().withY(1));
        }, countries);
    }
}
