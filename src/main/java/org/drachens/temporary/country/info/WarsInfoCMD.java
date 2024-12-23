package org.drachens.temporary.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class WarsInfoCMD extends Command {
    public WarsInfoCMD() {
        super("wars");

        var countries = ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof Player p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, getCountryNames(p.getInstance()));
                });

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null)
                return;
            List<Component> comps = new ArrayList<>();
            comps.add(Component.text()
                    .append(Component.text("______/", NamedTextColor.BLUE))
                    .append(country.getNameComponent())
                    .append(Component.text("\\______",NamedTextColor.BLUE))
                    .build());
            country.getWars().forEach(country1 -> {
                comps.add(Component.newline());
                comps.add(country1.getNameComponent());
            });
            if (!country.getWarJustifications().isEmpty()){
                comps.add(Component.newline());
                comps.add(Component.text()
                        .append(Component.text("-----",NamedTextColor.BLUE))
                        .append(Component.text("Justifications",NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text("-----",NamedTextColor.BLUE))
                        .build());

                country.getWarJustifications().forEach(justification-> {
                    WarJustification warJustification = country.getCreatingWarJustificationAgainst(justification);
                    comps.add(Component.text()
                            .appendNewline()
                            .append(warJustification.getAgainstCountry().getNameComponent())
                            .append(Component.text(" time left: "))
                            .append(Component.text(warJustification.getTimeLeft()))
                            .build());
                });
            }

            if (!country.getCompletedWarJustifications().isEmpty()){
                comps.add(Component.newline());
                comps.add(Component.text()
                        .append(Component.text("-",NamedTextColor.BLUE))
                        .append(Component.text("Completed-Justifications",NamedTextColor.GOLD, TextDecoration.BOLD))
                        .append(Component.text("-",NamedTextColor.BLUE))
                        .build());
                country.getCompletedWarJustifications().forEach(justification-> {
                    WarJustification warJustification = country.getCreatingWarJustificationAgainst(justification);
                    comps.add(Component.text()
                            .appendNewline()
                            .append(warJustification.getAgainstCountry().getNameComponent())
                            .append(Component.text(" expires: "))
                            .append(Component.text(warJustification.getExpires()))
                            .build());
                });
            }

            p.sendMessage(Component.text().append(comps).build());
        }, countries);
    }
}
