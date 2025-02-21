package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import java.util.ArrayList;
import java.util.List;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class InfoOptionsCMD extends Command {
    public InfoOptionsCMD() {
        super("options");

        Argument<String> countries = getCountriesArg();

        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            if (null == country) {
                p.sendMessage(Component.text("You need to join a country first", NamedTextColor.RED));
                return;
            }
            sendMsg(p, country);
        });

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == country) {
                p.sendMessage(Component.text("That is not a valid country", NamedTextColor.RED));
                return;
            }
            sendMsg(p, country);
        }, countries);
    }


    public void sendMsg(CPlayer p, Country country) {
        List<Component> comps = new ArrayList<>();
        String name = country.getName();
        if (p.getCountry() == country) {
            comps.add(Component.text()
                    .append(Component.text()
                            .append(Component.text("[BORDERS] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.showText(Component.text("Click to show the countries borders", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country info borders")))
                    .append(Component.text()
                            .append(Component.text(" [LOANS] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.showText(Component.text("Click to show the countries loans", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country info loan_info")))
                    .append(Component.text()
                            .append(Component.text(" [LAWS] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                            .hoverEvent(HoverEvent.showText(Component.text("Click to show the countries laws", NamedTextColor.GRAY)))
                            .clickEvent(ClickEvent.runCommand("/country info laws")))
                    .appendNewline()
                    .build());
        }

        comps.add(Component.text()
                        .append(Component.text()
                                .append(Component.text(" [WARS] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to show the countries wars", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.runCommand("/country info wars " + name)))
                        .append(Component.text()
                                .append(Component.text(" [PUPPETS] ", NamedTextColor.GOLD, TextDecoration.BOLD))
                                .hoverEvent(HoverEvent.showText(Component.text("Click to show the countries puppets", NamedTextColor.GRAY)))
                                .clickEvent(ClickEvent.runCommand("/country info puppets " + name)))
                .build());

        p.sendMessage(Component.text()
                .append(Component.text("___________/", NamedTextColor.BLUE))
                .append(Component.text("Info"))
                .append(Component.text("\\___________", NamedTextColor.BLUE))
                .appendNewline()
                .append(comps)
                .build());
    }
}
