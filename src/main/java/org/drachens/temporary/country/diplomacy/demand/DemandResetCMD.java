package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

public class DemandResetCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    public DemandResetCMD() {
        super("reset");
        setCondition((sender,s)->hasDemand(sender));
        var types1 = ArgumentType.String("types1")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (!hasDemand(sender))return;
                    suggestion.addEntry(new SuggestionEntry("demanded"));
                    suggestion.addEntry(new SuggestionEntry("offer"));
                });

        var types2 = ArgumentType.String("types2")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (!hasDemand(sender))return;
                    String start = context.get(types1);
                    if (start.equalsIgnoreCase("demanded"))
                    suggestion.addEntry(new SuggestionEntry("annexation"));
                    suggestion.addEntry(new SuggestionEntry("provinces"));
                    suggestion.addEntry(new SuggestionEntry("puppets"));
                    suggestion.addEntry(new SuggestionEntry("payments"));
                });

        addSyntax((sender,context)->{},types1);
        addSyntax((sender,context)->{},types1,types2);
    }
    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
    private boolean hasDemand(CommandSender sender){
        return isLeaderOfCountry(sender) && demandManager.isPlayerActive((Player) sender);
    }
}
