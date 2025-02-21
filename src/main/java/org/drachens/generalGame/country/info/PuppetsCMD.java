package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class PuppetsCMD extends Command {
    public PuppetsCMD() {
        super("puppets");

        var countries = getCountriesArg();

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            Country target = p.getCountry();
            if (target == null) {
                p.sendMessage("Join a country or do /country info puppet <country>");
                return;
            }
            List<Component> comps = new ArrayList<>();
            target.getDiplomacy().getPuppets().forEach(country -> comps.add(Component.text().append(Component.text(" - ")).append(country.getComponentName()).appendNewline().build()));
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(Component.text("Puppets", NamedTextColor.GOLD))
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(comps)
                    .build());
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            Country target = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (target == null) {
                p.sendMessage("That is not a valid country");
                return;
            }
            List<Component> comps = new ArrayList<>();
            target.getDiplomacy().getPuppets().forEach(country -> comps.add(Component.text().append(Component.text(" - ")).append(country.getComponentName()).appendNewline().build()));
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(Component.text("Puppets", NamedTextColor.GOLD))
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(comps)
                    .build());
        }, countries);
    }
}
