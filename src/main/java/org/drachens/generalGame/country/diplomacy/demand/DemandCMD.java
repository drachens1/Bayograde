package org.drachens.generalGame.country.diplomacy.demand;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

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
        addSubcommand(new DemandSetPeaceCMD());
        addSubcommand(new DemandOutgoingCMD());
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
