package org.drachens.cmd.Dev.Permissions;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.permission.Permission;
import org.drachens.Manager.defaults.ContinentalManagers;

import java.util.ArrayList;
import java.util.List;

public class add extends Command {
    public add() {
        super("add");
        setCondition(((sender, s) -> sender.hasPermission("editPermissions")));
        setDefaultExecutor((sender, s) -> {
            if (!sender.hasPermission("editPermissions")) return;
            sender.sendMessage("Proper usage: /permissions add ");
        });
        var groupName = ArgumentType.String("group name");
        var permissions = ArgumentType.StringArray("permissions");
        addSyntax((sender, context) -> {
            if (!sender.hasPermission("editPermissions")) return;
            List<Permission> perms = new ArrayList<>();
            for (String perm : context.get(permissions)) {
                perms.add(new Permission(perm));
            }
            ContinentalManagers.permissions.addPermissionGroup(context.get(groupName), perms, true);
        }, groupName, permissions);
    }
}
