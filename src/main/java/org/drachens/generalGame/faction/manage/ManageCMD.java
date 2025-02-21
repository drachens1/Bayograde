package org.drachens.generalGame.faction.manage;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.countryClass.Country;
import org.drachens.player_types.CPlayer;

public class ManageCMD extends Command {
    public ManageCMD() {
        super("manage");
        setCondition((sender, s) -> leaderOfAFaction(sender));
        addSubcommand(new InviteCMD());
        addSubcommand(new KickCMD());
        addSubcommand(new SetLeaderCMD());
        addSubcommand(new DeleteCMD());
        addSubcommand(new RenameCMD());
    }

    private boolean leaderOfAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return null != country && country.isLeaderOfAFaction();
        }
        return false;
    }
}
