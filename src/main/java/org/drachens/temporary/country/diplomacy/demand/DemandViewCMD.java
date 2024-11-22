package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import net.minestom.server.entity.Player;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.temporary.demand.WW2Demands;

public class DemandViewCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;

    public DemandViewCMD() {
        super("view");

        setCondition((sender, s) -> hasDemand(sender));

        var on = ArgumentType.String("")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!hasDemand(sender)) return;
                    suggestion.addEntry(new SuggestionEntry("on"));
                    suggestion.addEntry(new SuggestionEntry("off"));
                });

        addSyntax((sender, context) -> {
            if (!hasDemand(sender)) return;
            CPlayer p = (CPlayer) sender;
            WW2Demands demand = (WW2Demands) demandManager.getDemand(p);
            if (demand == null) return;
            switch (context.get(on)) {
                case "on":
                    demand.showPlayer(p);
                    inventoryManager.assignInventory(p, "demand");
                    break;
                case "off":
                    demand.hidePlayer(p);
                    inventoryManager.assignInventory(p, "default");
                    break;
            }
        }, on);

    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }

    private boolean hasDemand(CommandSender sender) {
        return isLeaderOfCountry(sender) && demandManager.isPlayerActive((Player) sender);
    }
}
