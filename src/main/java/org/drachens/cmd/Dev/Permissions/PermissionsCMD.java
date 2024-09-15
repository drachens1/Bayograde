package org.drachens.cmd.Dev.Permissions;

import net.minestom.server.command.builder.Command;

public class PermissionsCMD extends Command {
    public PermissionsCMD() {
        super("permissions");
        setCondition(((sender, s) -> sender.hasPermission("editPermissions")));
        setDefaultExecutor((sender, s) -> {
            if (!sender.hasPermission("editPermissions")) return;
            sender.sendMessage("Proper usage: ");
        });
        addSubcommand(new add());
    }
}
