package org.drachens.cmd.whitelist;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.WhitelistManager;

import java.util.UUID;

import static org.drachens.api.util.PlayerUtil.getUUIDFromName;

public class WhitelistAddCMD extends Command {
    public WhitelistAddCMD(WhitelistManager whitelistManager) {
        super("add");
        var player = ArgumentType.String("player");
        setCondition((sender, s) -> sender.hasPermission("whitelist"));
        setDefaultExecutor((sender,context)->{
            if (sender.hasPermission("whitelist"))sender.sendMessage("Usage /whitelist add <player>");
        });
        addSyntax((sender,context)->{
            UUID p = getUUIDFromName(context.get(player));
            whitelistManager.addPlayer(p);
            sender.sendMessage(context.get(player)+" was added to the whitelist");
        },player);
    }
}
