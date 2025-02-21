package org.drachens.generalGame.country.diplomacy.demand;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.Manager.DemandManager;
import org.drachens.Manager.InventoryManager;
import org.drachens.Manager.defaults.ContinentalManagers;
import org.drachens.Manager.defaults.enums.InventoryEnum;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.generalGame.demand.WW2Demands;
import org.drachens.player_types.CPlayer;

public class DemandViewCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;
    private final InventoryManager inventoryManager = ContinentalManagers.inventoryManager;

    public DemandViewCMD() {
        super("view");

        setCondition((sender, s) -> hasDemand(sender));

        Argument<String> on = ArgumentType.String("")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!hasDemand(sender)) return;
                    suggestion.addEntry(new SuggestionEntry("on"));
                    suggestion.addEntry(new SuggestionEntry("off"));
                });

        addSyntax((sender, context) -> {
            if (!hasDemand(sender)) return;
            CPlayer p = (CPlayer) sender;
            WW2Demands demand = (WW2Demands) demandManager.getDemand(p.getCountry());
            if (null == demand) return;
            switch (context.get(on)) {
                case "on":
                    demand.showPlayer(p);
                    inventoryManager.assignInventory(p, InventoryEnum.demand);
                    break;
                case "off":
                    demand.hidePlayer(p);
                    inventoryManager.assignInventory(p, InventoryEnum.defaultInv);
                    break;
            }
        }, on);

    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            return (null != country) && country.isPlayerLeader(p);
        }
        return false;
    }

    private boolean hasDemand(CommandSender sender) {
        CPlayer p = (CPlayer) sender;
        return isLeaderOfCountry(sender) && demandManager.isPlayerActive(p.getCountry());
    }
}
