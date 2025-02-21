package org.drachens.generalGame.country.loan;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

public class LoanCMD extends Command {
    public LoanCMD() {
        super("loan");
        addSubcommand(new LoanAcceptCMD());
        addSubcommand(new LoanCreateCMD());
        setCondition((sender, s) -> isLeaderOfCountry(sender));
    }

    private boolean isLeaderOfCountry(CommandSender sender) {
        if (sender instanceof CPlayer p) {
            Country country = p.getCountry();
            return (null != country) && country.isPlayerLeader(p);
        }
        return false;
    }
}
