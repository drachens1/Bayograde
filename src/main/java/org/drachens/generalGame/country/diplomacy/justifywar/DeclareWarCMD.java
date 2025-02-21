package org.drachens.generalGame.country.diplomacy.justifywar;

import net.minestom.server.command.builder.Command;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.events.countries.war.StartWarEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;


public class DeclareWarCMD extends Command {
    public DeclareWarCMD() {
        super("declare-war");
        var countries = getCountriesArgExcludingPlayersCountry();
        setDefaultExecutor((sender, context) -> {
            sender.sendMessage("Proper usage /country diplomacy declare_war <country> ");
        });

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country against = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (against == null) {
                p.sendMessage("That is not a valid country");
                return;
            }
            WarJustification warJustification = country.getDiplomacy().getCompletedWarJustification(against.getName());
            if (warJustification == null) {
                p.sendMessage("You do not have a completed justification against them");
                return;
            }
            EventDispatcher.call(new StartWarEvent(country, against, warJustification));
        }, countries);
    }
}
