package org.drachens.cmd.Dev.debug;

import dev.ng5m.CPlayer;
import net.minestom.server.command.builder.Command;

public class debugCMD extends Command {
    private final String permission = "debug";

    public debugCMD() {
        super("debug");
        setCondition((sender, s) ->{
            CPlayer p = (CPlayer) sender;
            return p.hasPermission(permission);
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (p.hasPermission(permission)) sender.sendMessage("Proper usage /debug <option>");
        });
        addSubcommand(new allCMD(permission));
    }
}
