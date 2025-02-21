package org.drachens.generalGame.country.diplomacy.nonaggression;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.events.countries.nonaggression.NonAggressionOfferEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class NonAggressionCreateCMD extends Command {
    public NonAggressionCreateCMD() {
        super("create");
        var countries = getCountriesArgExcludingPlayersCountry();

        var length = ArgumentType.Integer("length");
        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            p.sendMessage(Component.text("Proper usage /country diplomacy non-aggression create <target>", NamedTextColor.RED));
        }, countries);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country to = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (to == null) return;
            NonAggressionPact nonAggressionPact = new NonAggressionPact(country, to, context.get(length));
            EventDispatcher.call(new NonAggressionOfferEvent(nonAggressionPact));
        }, countries, length);
    }
}
