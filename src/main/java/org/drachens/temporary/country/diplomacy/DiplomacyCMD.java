package org.drachens.temporary.country.diplomacy;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.drachens.player_types.CPlayer;
import org.drachens.temporary.country.diplomacy.demand.DemandCMD;
import org.drachens.temporary.country.diplomacy.justifywar.DeclareWarCMD;
import org.drachens.temporary.country.diplomacy.justifywar.JustifyWarCMD;
import org.drachens.temporary.country.diplomacy.liberate.LiberateCMD;
import org.drachens.temporary.country.diplomacy.nonaggression.NonAggressionCMD;

public class DiplomacyCMD extends Command {
    public DiplomacyCMD() {
        super("diplomacy");
        addSubcommand(new DemandCMD());
        addSubcommand(new JustifyWarCMD());
        addSubcommand(new DeclareWarCMD());
        addSubcommand(new NonAggressionCMD());
        addSubcommand(new DiplomacyViewOptionsCMD());
        addSubcommand(new UnconditionalSurrenderCMD());
        addSubcommand(new LiberateCMD());
        setCondition((sender, s) -> isLeaderOfCountry(sender));
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
