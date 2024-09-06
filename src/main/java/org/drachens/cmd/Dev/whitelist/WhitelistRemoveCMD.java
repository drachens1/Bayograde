package org.drachens.cmd.Dev.whitelist;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import org.drachens.Manager.WhitelistManager;

import java.util.UUID;

import static org.drachens.util.PlayerUtil.getUUIDFromName;


public class WhitelistRemoveCMD extends Command{
    public WhitelistRemoveCMD(WhitelistManager whitelistManager) {
        super("remove");

        var player = ArgumentType.String("player");
        setCondition((sender, s) -> sender.hasPermission("whitelist"));
        setDefaultExecutor((sender,context)->{
            if (sender.hasPermission("whitelist"))sender.sendMessage("Usage /whitelist remove <player>");
        });
        addSyntax((sender,context)->{
            UUID p = getUUIDFromName(context.get(player));
            whitelistManager.removePlayer(p);
            sender.sendMessage(context.get(player)+" was removed from the whitelist");
        },player);
    }
}
