package org.drachens.generalGame.country.diplomacy.demand;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.dataClasses.Diplomacy.Demand;
import org.drachens.generalGame.demand.WW2Demands;
import org.drachens.player_types.CPlayer;

import java.util.Objects;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class DemandOutgoingCMD extends Command {
    public DemandOutgoingCMD() {
        super("out_going");
        var demands = ArgumentType.String("demands")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    getSuggestionBasedOnInput(suggestion, p.getCountry().getDiplomacy().getOutgoingDemands());
                });

        var choice = ArgumentType.String("choice")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    CPlayer p = (CPlayer) sender;
                    if (!p.getCountry().getDiplomacy().getDemandCountries().contains(context.get(demands))) return;
                    suggestion.addEntry(new SuggestionEntry("cancel"));
                    suggestion.addEntry(new SuggestionEntry("view"));
                });

        var third = ArgumentType.String("view")
                .setSuggestionCallback((sender, context, suggestion) -> {
                    if (Objects.equals(context.get(choice), "view")) {
                        suggestion.addEntry(new SuggestionEntry("off"));
                        suggestion.addEntry(new SuggestionEntry("on"));
                    }
                });

        setCondition((sender, s) -> hasSentADemand(sender));

        addSyntax((sender, context) -> {
        }, demands);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            Demand demand = country.getDiplomacy().getOutgoingDemand(context.get(demands));
            if (demand == null) return;
            switch (context.get(choice)) {
                case "cancel":
                    demand.getToCountry().removeDemand(demand, demand.getFromCountry().getName());
                case "view":
                    p.sendMessage("You need to choose on / off");
            }
        }, demands, choice);

        addSyntax((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();
            WW2Demands demand = (WW2Demands) country.getDiplomacy().getOutgoingDemand(context.get(demands));
            if (demand == null) return;
            switch (context.get(choice)) {
                case "cancel":
                    demand.getToCountry().removeDemand(demand, demand.getFromCountry().getName());
                    break;
                case "view":
                    switch (context.get(third)) {
                        case "on":
                            demand.showPlayer(p);
                            break;
                        case "off":
                            demand.hidePlayer(p);
                            break;
                    }
            }
        }, demands, choice, third);
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }

    private boolean hasSentADemand(CommandSender sender) {
        if (!isLeaderOfCountry(sender)) return false;
        CPlayer p = (CPlayer) sender;
        return p.getCountry().getDiplomacy().hasOutgoingDemands();
    }
}
