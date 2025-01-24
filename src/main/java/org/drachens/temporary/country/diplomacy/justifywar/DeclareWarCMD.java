package org.drachens.temporary.country.diplomacy.justifywar;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.Justifications.WarJustification;
import org.drachens.events.countries.war.StartWarEvent;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;


public class DeclareWarCMD extends Command {
    public DeclareWarCMD() {
        super("declare-war");
        var countries = getCountriesArgExcludingPlayersCountry();
        setDefaultExecutor((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            sender.sendMessage("Proper usage /country diplomacy declare_war <country> ");
        });

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) {
                sender.sendMessage("You are not the leader of a country");
                return;
            }
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country against = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (against == null) {
                p.sendMessage("That is not a valid country");
                return;
            }
            WarJustification warJustification = country.getCompletedWarJustificationAgainst(against);
            if (warJustification == null) {
                p.sendMessage("You do not have a completed justification against them");
                return;
            }
            EventDispatcher.call(new StartWarEvent(country, against, warJustification));
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
