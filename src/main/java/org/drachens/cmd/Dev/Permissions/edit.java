package org.drachens.cmd.Dev.Permissions;

import net.minestom.server.command.builder.Command;

public class edit extends Command {
    public edit() {
        super("add");
        setCondition(((sender, s) -> sender.hasPermission("editPermissions")));
        setDefaultExecutor((sender,s)->{
            if (!sender.hasPermission("editPermissions"))return;
            sender.sendMessage("Proper usage: /permissions add ");
        });
    }
}
