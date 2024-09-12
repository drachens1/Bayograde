package org.drachens.cmd.Dev.gamemode;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;

public class SurvivalCMD extends Command {
    public SurvivalCMD() {
        super("survival");
        setCondition((sender,s)->sender.hasPermission("gamemode"));
        setDefaultExecutor((sender,context)->{
            if (sender.hasPermission("gamemode")){
                Player p = (Player) sender;
                p.setGameMode(GameMode.SURVIVAL);
            }
        });
    }
}
