package org.drachens.cmd.country;

import dev.ng5m.CPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.events.Countries.CountryJoinEvent;

import static org.drachens.util.CommandsUtil.getCountryNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;
import static org.drachens.util.KyoriUtil.getPrefixes;

public class AcceptCMD extends Command {
    public AcceptCMD() {
        super("accept");

        var countries = ArgumentType.String("Countries");
        countries.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            getSuggestionBasedOnInput(suggestion, context.getInput(), 2, getCountryNames(p.getInstance()));
        });

        Component countryComp = getPrefixes("country");
        if (countryComp==null)return;
        Component notInvited = Component.text()
                .append(countryComp)
                .append(Component.text("this country has not invited you", NamedTextColor.RED))
                .build();
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country==null)
                return;
            if (!country.hasInvited(p)){
                p.sendMessage(notInvited);
                return;
            }
            country.addPlayer((CPlayer) p);
            EventDispatcher.call(new CountryJoinEvent(country, (CPlayer) p));
            country.removeInvite(p);
        }, countries);
    }
}
