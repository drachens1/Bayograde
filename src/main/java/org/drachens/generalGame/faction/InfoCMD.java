package org.drachens.generalGame.faction;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Diplomacy.faction.Faction;

import static org.drachens.util.CommandsUtil.getFactionNames;
import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class InfoCMD extends Command {
    public InfoCMD() {
        super("info");

        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /faction info <faction>"));

        var factions = ArgumentType.String("factionName")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    Player p = (Player) sender;
                    getSuggestionBasedOnInput(suggestion, getFactionNames(p.getInstance()));
                });

        addSyntax((sender, s) -> {
            Player p = (Player) sender;
            Faction faction1 = ContinentalManagers.world(p.getInstance()).countryDataManager().getFaction(s.get(factions));
            if (faction1 == null) {
                p.sendMessage("Faction not found");
                return;
            }
            p.sendMessage(faction1.getDescription());
        }, factions);
    }
}
