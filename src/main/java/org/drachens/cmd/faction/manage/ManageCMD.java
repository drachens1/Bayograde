package org.drachens.cmd.faction.manage;
import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;

public class ManageCMD extends Command {
    public ManageCMD() {
        super("manage");
        setCondition((sender,s)->leaderOfAFaction(sender));
        addSubcommand(new InviteCMD());
        addSubcommand(new KickCMD());
        addSubcommand(new SetLeaderCMD());
        addSubcommand(new DeleteCMD());
    }
    private boolean leaderOfAFaction(CommandSender sender) {
        if (sender instanceof CPlayer player) {
            Country country = player.getCountry();
            return country != null && country.isLeaderOfAFaction();
        }
        return false;
    }
}
