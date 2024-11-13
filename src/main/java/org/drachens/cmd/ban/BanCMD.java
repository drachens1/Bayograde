package org.drachens.cmd.ban;

import dev.ng5m.Constants;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.UUID;

import static org.drachens.util.PlayerUtil.getUUIDFromName;

public class BanCMD extends Command {
    public BanCMD() {
        super("ban");
        var player = ArgumentType.String("player");
        var reason = ArgumentType.StringArray("reason");
        var duration = ArgumentType.Long("time (minutes)");
        setCondition((sender, s) -> {
            return sender.hasPermission("ban");
        });
        setDefaultExecutor((sender, context) -> {
            if (sender.hasPermission("ban")) sender.sendMessage("Usage /ban <player>");
        });
        addSyntax((sender, context) -> {
            if (!sender.hasPermission("ban")) return;
            Player send = (Player) sender;
            System.out.println(send.getUsername() + " has banned " + context.get(player));
            UUID p = getUUIDFromName(context.get(player));
            if (p == null) {
                sender.sendMessage("Player is null");
                return;
            }
            send.sendMessage("You have banned " + context.get(player));
            Constants.BAN_MANAGER.banPlayer(p, context.get(duration));
        }, player, duration, reason);
    }
}
