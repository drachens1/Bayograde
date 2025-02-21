package org.drachens.generalGame.country;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InvitesEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.events.countries.CountryJoinEvent;
import org.drachens.player_types.CPlayer;
import org.drachens.util.MessageEnum;

import static org.drachens.util.CommandsUtil.getCountriesArg;

public class AcceptCMD extends Command {
    public AcceptCMD() {
        super("accept");

        var countries = getCountriesArg();
        setCondition((sender,command)->!inCountry(sender));

        Component notInvited = Component.text()
                .append(MessageEnum.country.getComponent())
                .append(Component.text("this country has not invited you", NamedTextColor.RED))
                .build();
        addSyntax((sender, context) -> {
            if (!(sender instanceof CPlayer p))
                return;
            Country country = ContinentalManagers.world(p.getInstance()).countryDataManager().getCountryFromName(context.get(countries));
            if (country == null)
                return;
            if (!country.getDiplomacy().containsInvite(InvitesEnum.player,p.getUsername())) {
                p.sendMessage(notInvited);
                return;
            }
            country.addPlayer(p);
            EventDispatcher.call(new CountryJoinEvent(country, p));
            country.getDiplomacy().removeInvite(InvitesEnum.player,p.getUsername());
        }, countries);
    }

    private boolean inCountry(CommandSender sender){
        CPlayer p = (CPlayer) sender;
        return p.getCountry()!=null;
    }
}
