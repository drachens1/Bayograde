package org.drachens.generalGame.country.info;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.builder.Command;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class GeneralCMD extends Command {
    public GeneralCMD() {
        super("general");

        var countries = getCountriesArg();

        setDefaultExecutor((sender, context) -> {
            if (!(sender instanceof CPlayer p)) {
                return;
            }
            Country country = p.getCountry();
            if (country == null) {
                p.sendMessage("Join a country or do /country info general <country>");
                return;
            }
            sendPlayer(country, p);
        });

        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null) {
                p.sendMessage("That is not a valid country");
                return;
            }
            sendPlayer(country, p);
        }, countries);
    }

    private void sendPlayer(Country country, CPlayer p) {
        Component c = country.getInfo().getDescription();
        if (country.getInfo().getPlayerLeader() == null) {
            p.sendMessage(c.appendNewline().append(Component.text("[JOIN]", NamedTextColor.GOLD, TextDecoration.BOLD)
                    .clickEvent(ClickEvent.runCommand("/country join " + country.getName()))
                    .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Click to join a country", NamedTextColor.GOLD)))));
        } else {
            p.sendMessage(c);
        }
    }
}
