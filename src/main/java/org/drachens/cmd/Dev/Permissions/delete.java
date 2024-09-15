package org.drachens.cmd.Dev.Permissions;

import net.minestom.server.command.builder.Command;

public class delete extends Command {
    public delete() {
        super("add");
        setCondition(((sender, s) -> sender.hasPermission("editPermissions")));
        setDefaultExecutor((sender, s) -> {
            if (!sender.hasPermission("editPermissions")) return;
            sender.sendMessage("Proper usage: /permissions add ");
        });
    }
}
