package org.drachens.temporary.country.diplomacy.nonaggression;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.events.countries.nonaggression.NonAggressionAcceptedEvent;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class NonAggressionCreateCMD extends Command {
    public NonAggressionCreateCMD() {
        super("create");
        var countries = ArgumentType.String("Countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, getCountryNames(p.getInstance()));
                });

        var length = ArgumentType.Float("length");

        addSyntax((sender,context)->{},countries);

        addSyntax((sender,context)->{
            if (!isLeaderOfCountry(sender))return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Country to = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (to==null)return;
            NonAggressionPact nonAggressionPact = new NonAggressionPact(country,to,context.get(length));
            EventDispatcher.call(new NonAggressionAcceptedEvent(nonAggressionPact));
        },countries,length);
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
