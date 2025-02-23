package org.drachens.generalGame.country.diplomacy;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.events.countries.war.UnconditionalSurrenderEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class UnconditionalSurrenderCMD extends Command {
    public UnconditionalSurrenderCMD() {
        super("unconditional_surrender");

        Argument<String> countriesAtWar = ArgumentType.String("Countries ")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getMilitary().getCountryWars());
                });

        addSyntax((sender, context) -> {
            sender.sendMessage("Proper usage /country diplomacy unconditional_surrender <country_at_war_with>");
        });

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country other = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countriesAtWar));
            if (null == other) {
                p.sendMessage("That is not a valid country");
                return;
            }
            if (!country.isAtWar(other.getName())) {
                p.sendMessage("You are not at war with that country");
                return;
            }
            EventDispatcher.call(new UnconditionalSurrenderEvent(country, other));
        }, countriesAtWar);
    }
}
