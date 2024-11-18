package org.drachens.temporary.country.diplomacy.demand;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.dataClasses.Countries.Country;

public class DemandCMD extends Command {
    public DemandCMD() {
        super("demand");
        setCondition((sender, s) -> isLeaderOfCountry(sender));

        addSubcommand(new DemandAddPaymentCMD());
        addSubcommand(new DemandExitCMD());
        addSubcommand(new DemandInfoCMD());
        addSubcommand(new DemandCompleteCMD());
        addSubcommand(new DemandResetCMD());
        addSubcommand(new DemandStartCMD());
        addSubcommand(new DemandIncomingCMD());
        addSubcommand(new DemandViewCMD());

        var reloader = ArgumentType.String("Type to Reload")
                .setSuggestionCallback((sender,context,suggestion)->{
                    CPlayer p = (CPlayer) sender;
                    p.refreshCommands();
                });
        addSyntax((sender,context)->{},reloader);
    }
    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
}
