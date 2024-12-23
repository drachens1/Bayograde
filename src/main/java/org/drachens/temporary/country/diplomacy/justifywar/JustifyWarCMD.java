package org.drachens.temporary.country.diplomacy.justifywar;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

public class JustifyWarCMD extends Command {
    public JustifyWarCMD() {
        super("justify_war");

        setCondition((sender,s)->isLeaderOfCountry(sender));

        addSubcommand(new JustifyAgainstCMD());
        addSubcommand(new JustifyCancelCMD());
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
