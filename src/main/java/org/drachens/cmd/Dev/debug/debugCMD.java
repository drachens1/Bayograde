package org.drachens.cmd.Dev.debug;

import net.minestom.server.command.builder.Command;

public class debugCMD extends Command {
    private final String permission = "debug";

    public debugCMD() {
        super("debug");
        setCondition((sender, s) -> sender.hasPermission(permission));
        setDefaultExecutor((sender, context) -> {
            if (sender.hasPermission(permission)) sender.sendMessage("Proper usage /debug <option>");
        });
        addSubcommand(new allCMD(permission));
    }
}
