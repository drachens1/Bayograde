package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;

public class DemandResetCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    public DemandResetCMD() {
        super("reset");
        setCondition((sender,s)->hasDemand(sender));
        var types = ArgumentType.String("types")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (!hasDemand(sender))return;
                });

        addSyntax((sender,context)->{},types);
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
