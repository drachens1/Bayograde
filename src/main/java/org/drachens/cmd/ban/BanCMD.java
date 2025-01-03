package org.drachens.cmd.ban;

import dev.ng5m.CPlayer;
import dev.ng5m.Constants;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.util.UUID;

import static org.drachens.util.PlayerUtil.getUUIDFromName;

public class BanCMD extends Command {
    public BanCMD() {
        super("ban");
        var player = ArgumentType.String("player");
        var reason = ArgumentType.StringArray("reason");
        var duration = ArgumentType.Long("time (minutes)");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("ban");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            if (p.hasPermission("ban")) sender.sendMessage("Usage /ban <player>");
        });
        addSyntax((sender, context) -> {
            CPlayer send = (CPlayer) sender;
            if (!send.hasPermission("ban")) return;
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
