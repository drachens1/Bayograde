package org.drachens.cmd;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public class TeleportCMD extends Command {
    public TeleportCMD() {
        super("teleport", "tp");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /teleport <x> <y> <z>"));
        ArgumentInteger x = ArgumentType.Integer("x");
        ArgumentInteger y = ArgumentType.Integer("y");
        ArgumentInteger z = ArgumentType.Integer("z");
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            Pos ps = new Pos(context.get(x), 1, context.get(z));
            p.teleport(ps);
        }, x, z);

        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            Pos ps = new Pos(context.get(x), context.get(y), context.get(z));
            p.teleport(ps);
        }, x, y, z);
    }
}
