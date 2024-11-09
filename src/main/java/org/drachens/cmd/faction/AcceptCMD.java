package org.drachens.cmd.faction;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.events.Factions.FactionJoinEvent;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class AcceptCMD extends Command {
    public AcceptCMD() {
        super("accept");

        setCondition((sender,s)->hasInvites(sender));

        var factionNames = ArgumentType.String("faction_name")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (!hasInvites(sender) && sender instanceof CPlayer player) {
                        getSuggestionBasedOnInput(suggestion,context.getInput(), 3,player.getCountry().getInvitedToFactions());
                    }
                });
        addSyntax((sender,context)->{
            if (!hasInvites(sender))return;
            CPlayer cPlayer = (CPlayer) sender;
            Factions factions = ContinentalManagers.world(cPlayer.getInstance()).countryDataManager().getFaction(context.get(factionNames));
            if (factions==null){
                cPlayer.sendMessage("Faction not found");
                return;
            }
            Country country = cPlayer.getCountry();
            if (factions.hasInvited(country)){
                factions.addCountry(country);
                EventDispatcher.call(new FactionJoinEvent(factions,country));
            }
        },factionNames);

    }
    private boolean hasInvites(CommandSender sender){
        if (sender instanceof CPlayer cPlayer){
            Country country =  cPlayer.getCountry();
            if (country==null)return false;
            return !country.getInvitedToFactions().isEmpty();
        }
        return false;
    }
}
