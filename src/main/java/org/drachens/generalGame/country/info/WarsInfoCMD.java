package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class WarsInfoCMD extends Command {
    public WarsInfoCMD() {
        super("wars");

        Argument<String> countries = getCountriesArg();

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            if (null == p.getCountry()) {
                p.sendMessage("You need to join a country or do /country info wars <country>");
                return;
            }
            Country country = p.getCountry();
            run(country, p);
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p)) return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == country) {
                p.sendMessage("That is not a valid country");
                return;
            }
            run(country, p);
        }, countries);
    }

    public void run(Country country, CPlayer p) {
        List<Component> comps = new ArrayList<>();
        comps.add(Component.text()
                .append(Component.text("______/", NamedTextColor.BLUE))
                .append(country.getComponentName())
                .append(Component.text("\\______", NamedTextColor.BLUE))
                .build());
        country.getMilitary().getCountryWars().forEach(war -> {
            comps.add(Component.newline());
            comps.add(ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(war).getComponentName());
        });
        if (!country.getDiplomacy().getWarJustificationCountries().isEmpty()) {
            comps.add(Component.newline());
            comps.add(Component.text()
                    .append(Component.text("-----", NamedTextColor.BLUE))
                    .append(Component.text("Justifications", NamedTextColor.GOLD, TextDecoration.BOLD))
                    .append(Component.text("-----", NamedTextColor.BLUE))
                    .build());

            country.getDiplomacy().getWarJustificationCountries().forEach(justification -> {
                WarJustification warJustification = country.getDiplomacy().getWarJustification(justification);
                comps.add(Component.text()
                        .appendNewline()
                        .append(warJustification.against().getComponentName())
                        .append(Component.text(" time left: "))
                        .append(Component.text(warJustification.timeLeft()))
                        .build());
            });
        }

        if (!country.getDiplomacy().getCompletedWarJustifications().isEmpty()) {
            comps.add(Component.newline());
            comps.add(Component.text()
                    .append(Component.text("-", NamedTextColor.BLUE))
                    .append(Component.text("Completed-Justifications", NamedTextColor.GOLD, TextDecoration.BOLD))
                    .append(Component.text("-", NamedTextColor.BLUE))
                    .build());
            country.getDiplomacy().getCompletedWarJustifications().forEach((c,justification) -> {
                WarJustification warJustification = country.getDiplomacy().getWarJustification(c);
                comps.add(Component.text()
                        .appendNewline()
                        .append(warJustification.against().getComponentName())
                        .append(Component.text(" expires: "))
                        .append(Component.text(warJustification.expires()))
                        .build());
            });
        }

        p.sendMessage(Component.text().append(comps).build());
    }
}
