package org.drachens.cmd.country.manage;
import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.drachens.dataClasses.Countries.Country;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KickCMD extends Command {
    public KickCMD() {
        super("accept");
        setCondition((sender,s)->isLeaderOfCountry(sender));
    }
    private boolean isLeaderOfCountry(CommandSender sender){
        if (sender instanceof CPlayer p){
            Country country = p.getCountry();
            if (country==null) return false;
            return country.isPlayerLeader(p);
        }
        return false;
    }
}
