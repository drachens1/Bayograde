package org.drachens.cmd.Fly;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentFloat;
import net.minestom.server.entity.Player;

public class FlyspeedCMD extends Command {
    public FlyspeedCMD() {
        super("flyspeed");
        ArgumentFloat speed = ArgumentType.Float("Flyspeed");
        setDefaultExecutor((sender, context) -> sender.sendMessage("Proper usage: /flyspeed <number>"));
        addSyntax((sender, context) -> {
            if (!(sender instanceof Player p)) {
                return;
            }
            p.setFlyingSpeed(context.get(speed));
        }, speed);
    }
}
