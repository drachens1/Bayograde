package org.drachens.cmd.faction;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;


public class Kick extends Command {
    public Kick() {
        super("kick");
        setCondition((sender,s)->requirements(sender));

    }

    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof CPlayer p)) return false;
        Country country = p.getCountry();
        if (country == null) return false;
        return country.isLeaderOfAFaction();
    }
}
