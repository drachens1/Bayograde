package org.drachens.cmd.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventDispatcher;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.dataClasses.Diplomacy.faction.Factions;
import org.drachens.events.Factions.FactionJoinEvent;
import org.drachens.interfaces.BetterCommand.IndividualCMD;

import static org.drachens.util.CommandsUtil.getFactionsSuggestionsBasedOnInput;
import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class JoinCMD extends IndividualCMD {
    public JoinCMD() {
        super("join");
        var factions = ArgumentType.String("Faction");
        factions.setSuggestionCallback((sender, context, suggestion) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            String[] a = context.getInput().split(" ");
            getFactionsSuggestionsBasedOnInput(suggestion, a[2], p.getInstance()).getEntries();
        });

        addSyntax((sender,context)->{
            if (!requirements(sender))
                return;
            Player p = (Player) sender;
            String name = context.get(factions);
            Factions faction = ContinentalManagers.world(p.getInstance()).countryDataManager().getFaction(name);
            if (faction==null){
                p.sendMessage("Cannot find that faction");
                return;
            }
            Country country = getCountryFromPlayer(p);
            country.setFaction(faction);
            EventDispatcher.call(new FactionJoinEvent(faction, country));
        },factions);
        setDefaultExecutor((sender,context)->sender.sendMessage("Proper usage /faction join <faction> "));
    }
    @Override
    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof Player p))return false;
        Country country = getCountryFromPlayer(p);
        return country != null;
    }
}
