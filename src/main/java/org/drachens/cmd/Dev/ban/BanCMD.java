package org.drachens.cmd.Dev.ban;

import dev.ng5m.Constants;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentStringArray;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentLong;
import org.drachens.player_types.CPlayer;

import java.util.UUID;

import static org.drachens.util.PlayerUtil.getUUIDFromName;

public class BanCMD extends Command {
    public BanCMD() {
        super("ban");
        ArgumentString player = ArgumentType.String("player");
        ArgumentStringArray reason = ArgumentType.StringArray("reason");
        ArgumentLong duration = ArgumentType.Long("time (minutes)");
        setCondition((sender, s) -> {
            CPlayer p = (CPlayer) sender;
            return p.hasPermission("ban");
        });
        setDefaultExecutor((sender, context) -> {
            CPlayer p = (CPlayer) sender;
            sender.sendMessage("Usage /ban <player>");
        });
        addSyntax((sender, context) -> {
            CPlayer send = (CPlayer) sender;
            System.out.println(send.getUsername() + " has banned " + context.get(player));
            UUID p = getUUIDFromName(context.get(player));
            if (null == p) {
                sender.sendMessage("Player is null");
                return;
            }
            send.sendMessage("You have banned " + context.get(player));
            Constants.BAN_MANAGER.banPlayer(p, context.get(duration));
        }, player, duration, reason);
    }
}
