package org.drachens.cmd.country.manage;
import dev.ng5m.CPlayer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CooperateCMD extends Command {
    public CooperateCMD() {
        super("co-op");
        setCondition((sender,s)->isLeaderOfCountry(sender));
    }
    private boolean isLeaderOfCountry(CommandSender sender){
        if (sender instanceof CPlayer p){
            return p.getCountry().isPlayerLeader(p);
        }
        return false;
    }
}
