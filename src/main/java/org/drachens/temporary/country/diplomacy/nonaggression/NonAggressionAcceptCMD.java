package org.drachens.temporary.country.diplomacy.nonaggression;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.enums.InvitesEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.NonAggressionPact;
import org.drachens.events.countries.nonaggression.NonAggressionAcceptedEvent;
import org.drachens.player_types.CPlayer;

import java.util.Set;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class NonAggressionAcceptCMD extends Command {
    public NonAggressionAcceptCMD() {
        super("accept");

        var countries = ArgumentType.String("countries")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!(sender instanceof CPlayer p)) {
                        return;
                    }
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getInvites(InvitesEnum.nonaggression));
                });

        setCondition((sender,command)->isLeaderOfCountry(sender));

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Set<String> invites = country.getInvites(InvitesEnum.nonaggression);
            String input = context.get(countries);
            if (!invites.contains(input)){
                p.sendMessage(Component.text("You do not have an invite from this source",NamedTextColor.RED));
                return;
            }
            NonAggressionPact nonAggressionPact = (NonAggressionPact) country.getInvite(InvitesEnum.nonaggression,input);
            if (nonAggressionPact==null){
                return;
            }
            country.removeInvite(InvitesEnum.nonaggression,input);
            EventDispatcher.call(new NonAggressionAcceptedEvent(nonAggressionPact));
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
