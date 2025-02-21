package org.drachens.generalGame.country.diplomacy.justifywar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;


public class JustifyOptionsCMD extends Command {
    public JustifyOptionsCMD() {
        super("options");

        Argument<String> countries = getCountriesArgExcludingPlayersCountry();

        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("proper usage /country diplomacy justify options <country>");
        });

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (null == country) {
                p.sendMessage("That country is not valid");
                return;
            }
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(Component.text("VIEW OPTIONS", NamedTextColor.GOLD))
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Country: "))
                    .append(country.getComponentName())
                    .appendNewline()
                    .append(Component.text()
                            .append(Component.text("Surprise ", TextColor.color(255, 0, 0)))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text()
                                    .append(Component.text("Effects: ")
                                            .appendNewline()
                                            .append(Component.text(" -55% stability"))
                                            .appendNewline()
                                            .append(Component.text(" 1 day to justify"))
                                            .appendNewline()
                                            .append(Component.text("Click to start justifying", NamedTextColor.GRAY, TextDecoration.ITALIC)))
                                    .build()))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy justify-war against surprise " + country.getName()))
                            .build())
                    .append(Component.text()
                            .append(Component.text("Partially Justified ", TextColor.color(255, 125, 0)))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text()
                                    .append(Component.text("Effects: ")
                                            .appendNewline()
                                            .append(Component.text(" -5% stability"))
                                            .appendNewline()
                                            .append(Component.text(" 85 day to justify"))
                                            .appendNewline()
                                            .append(Component.text("Click to start justifying", NamedTextColor.GRAY, TextDecoration.ITALIC)))
                                    .build()))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy justify-war against partially_justified " + country.getName()))
                            .build())
                    .append(Component.text()
                            .append(Component.text("Justified ", TextColor.color(0, 255, 0)))
                            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text()
                                    .append(Component.text("Effects: ")
                                            .appendNewline()
                                            .append(Component.text(" -0% stability"))
                                            .appendNewline()
                                            .append(Component.text(" 120 day to justify"))
                                            .appendNewline()
                                            .append(Component.text("Click to start justifying", NamedTextColor.GRAY, TextDecoration.ITALIC)))
                                    .build()))
                            .clickEvent(ClickEvent.runCommand("/country diplomacy justify-war against justified " + country.getName()))
                            .build())
                    .build());
        }, countries);
    }
}
