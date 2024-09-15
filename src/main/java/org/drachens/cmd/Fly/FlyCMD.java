package org.drachens.cmd.Fly;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class FlyCMD extends Command {
    public FlyCMD() {
        super("fly");
        var choice = ArgumentType.Boolean("True or false");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage /fly <boolean>"));
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            p.setFlying(context.get(choice));
        }, choice);
    }
}
