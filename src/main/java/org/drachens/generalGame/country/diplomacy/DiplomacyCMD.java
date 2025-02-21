package org.drachens.generalGame.country.diplomacy;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.generalGame.country.diplomacy.demand.DemandCMD;
import org.drachens.generalGame.country.diplomacy.justifywar.DeclareWarCMD;
import org.drachens.generalGame.country.diplomacy.justifywar.JustifyWarCMD;
import org.drachens.generalGame.country.diplomacy.liberate.LiberateCMD;
import org.drachens.generalGame.country.diplomacy.nonaggression.NonAggressionCMD;
import org.drachens.player_types.CPlayer;

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
