package org.drachens.cmd;

import net.minestom.server.command.builder.Command;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;

public class SpawnCMD extends Command {
    public SpawnCMD() {
        super("spawn");
        setDefaultExecutor((sender,context)->{
            if (!(sender instanceof Player p)){
                return;
            }
            p.teleport(new Pos(0,1,0));
        });
    }
}
