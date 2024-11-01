package org.drachens.cmd.faction;

import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;

import java.util.List;

import static org.drachens.util.PlayerUtil.getCountryFromPlayer;

public class Invite {
    public Invite() {

    }

    public void causes(CommandSender sender, String input) {

    }

    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof Player p))return false;
        Country country = getCountryFromPlayer(p);
        if (country == null)return false;
        return country.isLeaderOfAFaction();
    }

    public List<String> generateAutoComp(CommandSender sender) {
        return null;
    }
}
