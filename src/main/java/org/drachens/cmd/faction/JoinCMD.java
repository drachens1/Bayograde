package org.drachens.cmd.faction;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.events.Factions.FactionJoinEvent;

import static org.drachens.util.CommandsUtil.*;

public class JoinCMD extends Command {
    public JoinCMD() {
        super("join");
        setDefaultExecutor((sender,context)-> sender.sendMessage("Proper usage: /faction join <faction>"));
        setCondition((sender,s)-> notInAFaction(sender));

        var factions = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (notInAFaction(sender) && sender instanceof Player player) {
                        getSuggestionBasedOnInput(suggestion, context.getInput(), 2, getCountryNames(player.getInstance()));
                    }
                });


        addConditionalSyntax((sender, s) -> notInAFaction(sender), (sender, context)->{
            if (!notInAFaction(sender)) return;
            CPlayer player = (CPlayer) sender;

            Factions faction = ContinentalManagers.world(player.getInstance()).countryDataManager().getFaction(context.get(factions));
            if (faction == null) {
                player.sendMessage("Cannot find that faction.");
                return;
            }

            Country country = player.getCountry();
            country.joinFaction(faction);
            EventDispatcher.call(new FactionJoinEvent(faction, country));
        },factions);
    }

    private boolean notInAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && !country.isInAllFactions();
        }
        return false;
    }
}
