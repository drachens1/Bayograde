package org.drachens.cmd.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.interfaces.BetterCommand.IndividualCMD;

import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class Invite extends IndividualCMD {
    public Invite() {
        super("invite");
        setCondition((sender,s)->requirements(sender));
    }

    @Override
    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof Player p))return false;
        Country country = getCountryFromPlayer(p);
        if (country == null)return false;
        return country.isLeaderOfAFaction();
    }
}
