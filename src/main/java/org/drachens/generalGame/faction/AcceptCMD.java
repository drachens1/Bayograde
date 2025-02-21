package org.drachens.generalGame.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InvitesEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.faction.Faction;
import org.drachens.events.factions.FactionJoinEvent;
import org.drachens.player_types.CPlayer;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class AcceptCMD extends Command {
    public AcceptCMD() {
        super("accept");

        setCondition((sender, s) -> hasInvites(sender));

        Argument<String> factionNames = ArgumentType.String("faction_name")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (sender instanceof CPlayer player) {
                        getSuggestionBasedOnInput(suggestion, player.getCountry().getDiplomacy().getInviteKeys(InvitesEnum.faction));
                    }
                });

        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /faction accept <faction>"));

        addSyntax((sender, context) -> {
            CPlayer cPlayer = (CPlayer) sender;
            Faction faction = ContinentalManagers.world(cPlayer.getInstance()).countryDataManager().getFaction(context.get(factionNames));
            if (null == faction) {
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
            return (null != country) && country.getDiplomacy().getInviteKeys(InvitesEnum.faction).isEmpty();
        }
        return false;
    }
}
