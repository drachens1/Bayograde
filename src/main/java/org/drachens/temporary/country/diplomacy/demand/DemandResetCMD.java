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
import org.drachens.temporary.demand.WW2Demands;

public class DemandResetCMD extends Command {
    private final DemandManager demandManager = ContinentalManagers.demandManager;

    public DemandResetCMD() {
        super("reset");
        setCondition((sender, s) -> hasDemand(sender));
        var types1 = ArgumentType.String("types1")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!hasDemand(sender)) return;
                    suggestion.addEntry(new SuggestionEntry("demanded"));
                    suggestion.addEntry(new SuggestionEntry("offer"));
                });

        var types2 = ArgumentType.String("types2")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (!hasDemand(sender)) return;
                    String start = context.get(types1);
                    if (!(start.equalsIgnoreCase("demanded") || start.equalsIgnoreCase("offer"))) return;
                    suggestion.addEntry(new SuggestionEntry("annexation"));
                    suggestion.addEntry(new SuggestionEntry("provinces"));
                    suggestion.addEntry(new SuggestionEntry("puppets"));
                    suggestion.addEntry(new SuggestionEntry("payments"));
                });

        addSyntax((sender, context) -> {
        }, types1);
        addSyntax((sender, context) -> {
            if (!hasDemand(sender)) return;
            CPlayer p = (CPlayer) sender;
            WW2Demands ww2Demands = (WW2Demands) demandManager.getDemand(p);
            boolean demand;
            switch (context.get(types1)) {
                case "demanded":
                    demand = true;
                    break;
                case "offer":
                    demand = false;
                    break;
                default:
                    return;
            }
            switch (context.get(types2)) {
                case "annexation":
                    if (demand) {
                        ww2Demands.resetDemandedAnnexation();
                    } else
                        ww2Demands.resetOfferAnnexation();
                    break;
                case "provinces":
                    if (demand) {
                        ww2Demands.resetDemandedProvinces();
                    } else
                        ww2Demands.resetOfferProvinces();
                    break;
                case "puppets":
                    if (demand) {
                        ww2Demands.resetDemandedPuppets();
                    } else
                        ww2Demands.resetOfferPuppets();
                    break;
                case "payments":
                    if (demand) {
                        ww2Demands.resetDemandedPayments();
                    } else
                        ww2Demands.resetOfferPayments();
                    break;
            }
        }, types1, types2);
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
