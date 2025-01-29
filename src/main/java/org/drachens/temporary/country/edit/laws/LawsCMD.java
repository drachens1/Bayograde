package org.drachens.temporary.country.edit.laws;

import org.drachens.player_types.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.temporary.country.info.LawsInfo;

public class LawsCMD extends Command {
    public LawsCMD() {
        super("laws");

        setCondition((sender, s) -> isLeaderOfCountry(sender));

        addSubcommand(new LawsInfo("info"));
        addSubcommand(new LawsChangeOptionsCMD());
        addSubcommand(new LawsChangeCMD());
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
