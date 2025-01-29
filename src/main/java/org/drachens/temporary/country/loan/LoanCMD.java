package org.drachens.temporary.country.loan;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

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
            if (country == null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
}
