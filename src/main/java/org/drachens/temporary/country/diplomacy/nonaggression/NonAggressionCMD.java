package org.drachens.temporary.country.diplomacy.nonaggression;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;

public class NonAggressionCMD extends Command {
    public NonAggressionCMD() {
        super("non-aggression-pact");

        setCondition((sender, s) -> isLeaderOfCountry(sender));
        addSubcommand(new NonAggressionCreateCMD());
        addSubcommand(new NonAggressionAcceptCMD());
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
