package org.drachens.temporary.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class WarsInfoCMD extends Command {
    public WarsInfoCMD() {
        super("wars");

        var countries = ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    System.out.println("1");
                    if (!(sender instanceof Player p)) {
                        return;
                    }
                    System.out.println("2");
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
                comps.add(country1.getNameComponent());
            });
            p.sendMessage(Component.text().append(comps).build());
        }, countries);
    }
}
