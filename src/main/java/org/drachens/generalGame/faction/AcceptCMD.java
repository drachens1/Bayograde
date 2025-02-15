package org.drachens.generalGame.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InvitesEnum;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.events.factions.FactionJoinEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class AcceptCMD extends Command {
    public AcceptCMD() {
        super("accept");

        setCondition((sender, s) -> hasInvites(sender));

        var factionNames = ArgumentType.String("faction_name")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (hasInvites(sender) && sender instanceof CPlayer player) {
                        getSuggestionBasedOnInput(suggestion, player.getCountry().getInvites(InvitesEnum.faction));
                    }
                });

        setDefaultExecutor((sender, context) -> {
            if (hasInvites(sender)) {
                sender.sendMessage("Proper usage /faction accept <faction>");
            }
        });

        addSyntax((sender, context) -> {
            if (!hasInvites(sender)) return;
            CPlayer cPlayer = (CPlayer) sender;
            Faction faction = ContinentalManagers.world(cPlayer.getInstance()).countryDataManager().getFaction(context.get(factionNames));
            if (faction == null) {
                cPlayer.sendMessage("Faction not found");
                return;
            }
            Country country = cPlayer.getCountry();
            if (faction.hasInvited(country)) {
                faction.addCountry(country);
                EventDispatcher.call(new FactionJoinEvent(faction, country));
            }
        }, factionNames);

    }

    private boolean hasInvites(CommandSender sender) {
        if (sender instanceof CPlayer cPlayer) {
            Country country = cPlayer.getCountry();
            if (country == null) return false;
            return country.getInvites(InvitesEnum.faction).isEmpty();
        }
        return false;
    }
}
