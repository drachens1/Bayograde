package org.drachens.temporary.country.edit.ideology;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

public class IdeologiesChangeOptionsCMD extends Command {
    public IdeologiesChangeOptionsCMD() {
        super("change-options");

        setCondition((sender,s)->isLeaderOfCountry(sender));


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
