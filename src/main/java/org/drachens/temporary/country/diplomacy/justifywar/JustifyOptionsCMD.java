package org.drachens.temporary.country.diplomacy.justifywar;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;


public class JustifyOptionsCMD extends Command {
    public JustifyOptionsCMD() {
        super("options");

        var countries = getCountriesArgExcludingPlayersCountry();

        setDefaultExecutor((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            sender.sendMessage("proper usage /country diplomacy justify options <country>");
        });

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) {
                sender.sendMessage("You are not the leader of a country");
                return;
            }
            CPlayer p = (CPlayer) sender;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null) {
                p.sendMessage("That country is not valid");
                return;
            }
            p.sendMessage(Component.text()
                    .append(Component.text("_______/", NamedTextColor.BLUE))
                    .append(Component.text("VIEW OPTIONS", NamedTextColor.GOLD))
                    .append(Component.text("\\_______", NamedTextColor.BLUE))
                    .appendNewline()
                    .append(Component.text("Country: "))
                    .append(country.getNameComponent())
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

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
}
