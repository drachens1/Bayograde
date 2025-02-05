package org.drachens.temporary.country.diplomacy.nonaggression;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.events.countries.nonaggression.NonAggressionOfferEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class NonAggressionCreateCMD extends Command {
    public NonAggressionCreateCMD() {
        super("create");
        var countries = getCountriesArgExcludingPlayersCountry();

        var length = ArgumentType.Float("length");

        setCondition((sender,command)->isLeaderOfCountry(sender));

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            p.sendMessage(Component.text("Proper usage /country diplomacy non-aggression create <target>", NamedTextColor.RED));
        }, countries);

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country to = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (to == null) return;
            NonAggressionPact nonAggressionPact = new NonAggressionPact(country, to, context.get(length));
            EventDispatcher.call(new NonAggressionOfferEvent(nonAggressionPact));
        }, countries, length);
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
