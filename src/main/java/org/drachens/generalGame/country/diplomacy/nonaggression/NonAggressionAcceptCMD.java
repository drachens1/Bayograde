package org.drachens.generalGame.country.diplomacy.nonaggression;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.enums.InvitesEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.events.countries.nonaggression.NonAggressionAcceptedEvent;
import org.drachens.player_types.CPlayer;

import java.util.Set;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class NonAggressionAcceptCMD extends Command {
    public NonAggressionAcceptCMD() {
        super("accept");

        Argument<String> countries = ArgumentType.String("countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getDiplomacy().getInviteKeys(InvitesEnum.nonaggression));
                });

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Set<String> invites = country.getDiplomacy().getInviteKeys(InvitesEnum.nonaggression);
            String input = context.get(countries);
            if (!invites.contains(input)){
                p.sendMessage(Component.text("You do not have an invite from this source",NamedTextColor.RED));
                return;
            }
            NonAggressionPact nonAggressionPact = (NonAggressionPact) country.getDiplomacy().getInvite(InvitesEnum.nonaggression,input);
            if (null == nonAggressionPact) {
                return;
            }
            country.getDiplomacy().removeInvite(InvitesEnum.nonaggression,input);
            EventDispatcher.call(new NonAggressionAcceptedEvent(nonAggressionPact));
        }, countries);
    }
}
