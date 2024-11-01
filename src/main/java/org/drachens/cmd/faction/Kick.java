package org.drachens.cmd.faction;

import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.entity.Player;
import org.drachens.dataClasses.Countries.Country;

import java.util.List;


public class Kick {
    public Kick() {
    }

    public void causes(CommandSender sender, String input) {

    }

    public boolean requirements(CommandSender sender) {
        if (!(sender instanceof CPlayer p)) return false;
        Country country = p.getCountry();
        if (country == null) return false;
        return country.isLeaderOfAFaction();
    }

    public List<String> generateAutoComp(CommandSender sender) {
        return null;
    }
}
