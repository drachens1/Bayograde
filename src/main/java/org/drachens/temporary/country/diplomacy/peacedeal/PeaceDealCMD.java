package org.drachens.temporary.country.diplomacy.peacedeal;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

import static org.drachens.util.CommandsUtil.getCountriesArgExcludingPlayersCountry;

public class PeaceDealCMD extends Command {
    public PeaceDealCMD() {
        super("peace-deal");

        var countries = getCountriesArgExcludingPlayersCountry();

        addSyntax((sender, context) -> {
            if (!isLeaderOfCountry(sender)) return;
            CPlayer p = (CPlayer) sender;
            Country country = p.getCountry();

        }, countries);
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
