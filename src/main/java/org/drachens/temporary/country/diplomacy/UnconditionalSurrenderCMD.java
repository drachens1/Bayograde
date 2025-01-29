package org.drachens.temporary.country.diplomacy;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.events.countries.war.UnconditionalSurrenderEvent;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class UnconditionalSurrenderCMD extends Command {
    public UnconditionalSurrenderCMD() {
        super("unconditional_surrender");

        var countriesAtWar = ArgumentType.String("Countries ")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getCountryWars());
                });

        setCondition((sender, s) -> isLeaderOfCountry(sender));

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            sender.sendMessage("Proper usage /country diplomacy unconditional_surrender <country_at_war_with>");
        });

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country other = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countriesAtWar));
            if (other == null) {
                p.sendMessage("That is not a valid country");
                return;
            }
            if (!country.isAtWar(other)) {
                p.sendMessage("You are not at war with that country");
                return;
            }
            EventDispatcher.call(new UnconditionalSurrenderEvent(country, other));
        }, countriesAtWar);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p) && country.isInAWar();
        }
        return false;
    }
}
