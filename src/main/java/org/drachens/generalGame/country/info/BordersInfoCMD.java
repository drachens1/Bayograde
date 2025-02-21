package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.per_instance.CountryDataManager;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class BordersInfoCMD extends Command {
    public BordersInfoCMD() {
        super("borders");


        Argument<String> countries = getCountriesArg();

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            Country country = p.getCountry();
            if (null == country) {
                p.sendMessage("Join a country or do /country info general <country>");
                return;
            }
            sendPlayer(country, p);
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == country) {
                p.sendMessage("That is not a valid country");
                return;
            }
            sendPlayer(country, p);
        }, countries);
    }

    private void sendPlayer(Country country, CPlayer p) {
        List<Component> comps = new ArrayList<>();
        CountryDataManager c = ContinentalManagers.world(p.getInstance()).countryDataManager();
        country.getMilitary().getBorders().forEach(string -> comps.add(c.getCountryFromName(string).getComponentName()));
        p.sendMessage(Component.text()
                .append(comps)
                .build());
    }
}
