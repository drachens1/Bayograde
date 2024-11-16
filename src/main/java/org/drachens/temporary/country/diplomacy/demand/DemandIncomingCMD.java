package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.suggestion.SuggestionEntry;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getSuggestionBasedOnInput;

public class DemandIncomingCMD extends Command {
    public DemandIncomingCMD() {
        super("incoming");
        setCondition((sender,s)->hasDemandSent(sender));

        var options = ArgumentType.String("Demands")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (!hasDemandSent(sender))return;
                    CPlayer p = (CPlayer) sender;
                    getSuggestionBasedOnInput(suggestion, context.getInput(), 5, p.getCountry().getDemandCountryNames());
                });

        var choice = ArgumentType.String("choice")
                .setSuggestionCallback((sender,context,suggestion)->{
                    if (!hasDemandSent(sender))return;
                    CPlayer p = (CPlayer) sender;
                    if (!p.getCountry().getDemandCountryNames().contains(context.get(options)))return;
                    suggestion.addEntry(new SuggestionEntry("accept"));
                    suggestion.addEntry(new SuggestionEntry("deny"));
                    suggestion.addEntry(new SuggestionEntry("counter-offer"));
                });

        addSyntax((sender,context)->{},options);

        addSyntax((sender,context)->{

        },options,choice);
    }
    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
    private boolean hasDemandSent(CommandSender sender){
        if (!isLeaderOfCountry(sender))return false;
        CPlayer p = (CPlayer)sender;
        return p.getCountry().hasAnyDemands();
    }
}
