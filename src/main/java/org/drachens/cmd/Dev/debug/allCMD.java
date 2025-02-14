package org.drachens.cmd.Dev.debug;

import net.minestom.server.command.builder.Command;
import org.drachens.player_types.CPlayer;

public class allCMD extends Command {
    public allCMD(String permission) {
        super("all");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission(permission);
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (p.hasPermission(permission)) sender.sendMessage("Proper usage /debug <option>");
        });
    }
}
