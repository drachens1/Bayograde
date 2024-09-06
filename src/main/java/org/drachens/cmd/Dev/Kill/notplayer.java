package org.drachens.cmd.Dev.Kill;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;

public class notplayer extends Command {
    public notplayer() {
        super("!player");
        setCondition((sender,s)->sender.hasPermission("kill"));
        setDefaultExecutor((sender,context)->{
            if (!sender.hasPermission("kill"))return;
            Player p = (Player) sender;
            for (Entity e : p.getInstance().getEntities()){
                if (!(e instanceof Player))e.remove();
            }
        });
    }
}
