package org.drachens.cmd.Fly;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class FlyspeedCMD extends Command {
    public FlyspeedCMD() {
        super("flyspeed");
        var speed = ArgumentType.Integer("0-20");
        setDefaultExecutor((sender,context)->{
            sender.sendMessage("Proper usage: /flyspeed <number>");
        });
        addSyntax((sender,context)->{
            if (!(sender instanceof Player p)){
                return;
            }
            p.setFlyingSpeed(context.get(speed));
        },speed);
    }
}
