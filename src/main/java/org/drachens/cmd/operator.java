package org.drachens.cmd;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import static org.drachens.api.util.PermissionsUtil.playerOp;

public class operator extends Command {
    public operator() {
        super("operator", "op");
        setDefaultExecutor((sender,context)-> sender.sendMessage("Usage /op <player>"));

        var player = ArgumentType.Entity("player");
        addSyntax((sender,context)->{
            Player p = context.get(player).findFirstPlayer(sender);
            if (p == null){
                sender.sendMessage("P is null");
                return;
            }
            sender.sendMessage("You have opped "+p.getUsername());
            p.sendMessage("You have been opped");
            playerOp(p);
        },player);
    }
}
