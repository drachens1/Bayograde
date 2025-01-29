package org.drachens.temporary.country.edit.ideology;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

public class IdeologiesCMD extends Command {
    public IdeologiesCMD() {
        super("ideologies");

        setCondition((sender, s) -> isLeaderOfCountry(sender));

        addSubcommand(new IdeologiesChangeOptionsCMD());
        addSubcommand(new IdeologiesBoostCMD());
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
