package org.drachens.cmd.ban;

import dev.ng5m.CPlayer;
import dev.ng5m.Constants;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.UUID;

import static org.drachens.util.PlayerUtil.getUUIDFromName;

public class UnbanCMD extends Command {
    public UnbanCMD() {
        super("unban");
        var player = ArgumentType.String("player");
        setCondition((sender, s) -> sender.hasPermission("unban"));
        setDefaultExecutor((sender, context) -> {
            if (sender.hasPermission("unban")) sender.sendMessage("Usage: /unban player");
        });
        addSyntax((sender, context) -> {
            if (!sender.hasPermission("unban")) return;
            Player p = (CPlayer) sender;
            System.out.println(p.getUsername() + " has unbanned " + context.get(player));
            UUID u = getUUIDFromName(context.get(player));
            if (u == null) {
                sender.sendMessage("Player is null");
                return;
            }
            p.sendMessage("You have unbanned " + context.get(player));
            Constants.BAN_MANAGER.removeEntry(p.getUuid());
        }, player);
    }
}